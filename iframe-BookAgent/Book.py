# coding=gbk

import streamlit as st
import os
import openai
import backoff
import pandas as pd
import numpy as np

openai.api_key = "your api key"

st.set_page_config(
    page_title="图书助理",
    page_icon="./image/labor.png",
    layout="centered",
    initial_sidebar_state="auto",
)

def init_sidebar():
    """
    初始化侧边栏
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

    # 添加联系信息或社交媒体链接
    st.sidebar.subheader("联系作者")
    st.sidebar.markdown("""
        如果你有任何问题或建议，请通过以下方式联系我：
        - 邮箱: echoaugust2@gmail.com
        - 电话: 151-5201-2670
        - [GitHub](https://github.com/August6676/-Agent)
    """)

# 调用初始化侧边栏函数
init_sidebar()

def init_content():
    """
    初始化内容
    :return:
    """
    st.image("./image/Mini-Lib.png", width=300)
    st.title('图书助理 :blue[_Agent_]')

    st.markdown('''
        :rainbow[图书助理 Agent，知道有关图书的信息以及与人类进行沟通，帮助您使用Mini-Lib!] ''')

    # 输入框，让用户输入内容
    st.header("输入")
    text_area = st.text_area("", "在这里输入你的需求~~~~~~~~比如 你是谁？", height=100, max_chars=200)

    # 如果文本内容等于“你是谁？”，则输出“我是劳模智能体，我可以讲述相关劳模的事迹以及与人类进行沟通，可以作为劳模学习和教学的辅助工具。”
    # 写一个标题
    st.header("输出")
    text = st.empty()

    if text_area == "你是谁？":
        st.write("我是您的图书助理。您可以问我有关图书的信息，比方说···的作者是谁？···这本书讲述了什么内容？我将会尽我所能给您回答！")
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
            {"role": "system", "content": "你是一个图书助理，了解有关图书的信息，你将会回答用户提的问题。"},
            {"role": "user", "content": text_area},
        ]
        max_tokens = 512
        temperature = 0.9
        stop_words = []
        response = call_lm(model, messages, max_tokens, temperature, stop_words)
        st.write(response)

    # 假设的起始日期为5月10日，并向后推14天
    start_date = '2024-05-10'
    end_date = pd.Timestamp(start_date) + pd.DateOffset(days=14)

    # 示例访问量数据，使用上述日期范围
    data = {
        '日期': pd.date_range(start=start_date, end=end_date, freq='D'),
        '访问量': np.random.randint(10, 50, size=len(pd.date_range(start=start_date, end=end_date, freq='D')))
    }
    df = pd.DataFrame(data)
    df = df.set_index('日期')

    # streamlit 部分
    st.subheader('近期访问量')
    st.area_chart(df)

if __name__ == '__main__':
    init_sidebar()
    init_content()
