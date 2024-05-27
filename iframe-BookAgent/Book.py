# coding=gbk

import streamlit as st
import os
import openai
import backoff
import pandas as pd
import numpy as np

openai.api_key = "your api key"

st.set_page_config(
    page_title="ͼ������",
    page_icon="./image/labor.png",
    layout="centered",
    initial_sidebar_state="auto",
)

def init_sidebar():
    """
    ��ʼ�������
    :return:
    """
    st.sidebar.markdown(
        """
        <style>
        .sidebar-title {
            font-size: 30px;
            font-weight: bold;
            color: #333333;
            text-align: center;
            margin-top: 20px;
        }
        </style>
        """,
        unsafe_allow_html=True
    )

    # �����ϵ��Ϣ���罻ý������
    st.sidebar.subheader("��ϵ����")
    st.sidebar.markdown("""
        ��������κ�������飬��ͨ�����·�ʽ��ϵ�ң�
        - ����: echoaugust2@gmail.com
        - �绰: 151-5201-2670
        - [GitHub](https://github.com/August6676/-Agent)
    """)

# ���ó�ʼ�����������
init_sidebar()

def init_content():
    """
    ��ʼ������
    :return:
    """
    st.image("./image/Mini-Lib.png", width=300)
    st.title('ͼ������ :blue[_Agent_]')

    st.markdown('''
        :rainbow[ͼ������ Agent��֪���й�ͼ�����Ϣ�Լ���������й�ͨ��������ʹ��Mini-Lib!] ''')

    # ��������û���������
    st.header("����")
    text_area = st.text_area("", "�����������������~~~~~~~~���� ����˭��", height=100, max_chars=200)

    # ����ı����ݵ��ڡ�����˭�������������������ģ�����壬�ҿ��Խ��������ģ���¼��Լ���������й�ͨ��������Ϊ��ģѧϰ�ͽ�ѧ�ĸ������ߡ���
    # дһ������
    st.header("���")
    text = st.empty()

    if text_area == "����˭��":
        st.write("��������ͼ�����������������й�ͼ�����Ϣ���ȷ�˵��������������˭���������Ȿ�齲����ʲô���ݣ��ҽ��ᾡ�����ܸ����ش�")
    else:
        @backoff.on_exception(
            backoff.fibo,
            (
                openai.error.APIError,
                openai.error.Timeout,
                openai.error.RateLimitError,
                openai.error.ServiceUnavailableError,
                openai.error.APIConnectionError,
                KeyError,
            ),
        )
        def call_lm(model, messages, max_tokens, temperature, stop_words):
            try:
                response = openai.ChatCompletion.create(
                    model=model,
                    messages=messages,
                    max_tokens=max_tokens,
                    temperature=temperature,
                    stop=stop_words
                )
                return response.choices[0].message["content"].strip()
            except Exception as e:
                st.error(f"Error: {e}")
                raise

        model = "gpt-3.5-turbo"
        messages = [
            {"role": "system", "content": "����һ��ͼ�������˽��й�ͼ�����Ϣ���㽫��ش��û�������⡣"},
            {"role": "user", "content": text_area},
        ]
        max_tokens = 512
        temperature = 0.9
        stop_words = []
        response = call_lm(model, messages, max_tokens, temperature, stop_words)
        st.write(response)

    # �������ʼ����Ϊ5��10�գ��������14��
    start_date = '2024-05-10'
    end_date = pd.Timestamp(start_date) + pd.DateOffset(days=14)

    # ʾ�����������ݣ�ʹ���������ڷ�Χ
    data = {
        '����': pd.date_range(start=start_date, end=end_date, freq='D'),
        '������': np.random.randint(10, 50, size=len(pd.date_range(start=start_date, end=end_date, freq='D')))
    }
    df = pd.DataFrame(data)
    df = df.set_index('����')

    # streamlit ����
    st.subheader('���ڷ�����')
    st.area_chart(df)

if __name__ == '__main__':
    init_sidebar()
    init_content()
