import { reactive } from 'vue'
import { defineStore } from 'pinia'

export const useStore = defineStore('store', () => {
  const auth = reactive({
    user: null
  })

  // 添加收藏的状态
  const favorites = reactive([]);

  // 添加收藏书籍的方法
  const addFavorite = (book) => {
    if (!favorites.some(fav => fav.id === book.id)) {
      favorites.push(book);
    }
  }

  // 判断书籍是否已收藏
  const isFavorite = (bookId) => {
    return favorites.some(fav => fav.id === bookId);
  }

  // 添加删除收藏的方法
  const removeFavorite = (bookId) => {
    favorites.splice(favorites.findIndex(fav => fav.id === bookId), 1);
  }

  return { auth, favorites, addFavorite, removeFavorite, isFavorite}
})
