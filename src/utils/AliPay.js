import axios from 'axios'

const API = axios.create({
    baseURL: "http://124.222.121.87:5049",
    timeout: 30000
})

export default API