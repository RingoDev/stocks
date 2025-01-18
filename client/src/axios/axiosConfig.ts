import axios from 'axios';
import {BACKEND_URL} from "../app/environment/evironment";




export const axiosInstance = axios.create(
    {
        withCredentials:true,
        baseURL:BACKEND_URL
    }
)