import {
    LOGIN_ERROR,
    LOGIN_PENDING,
    LOGIN_SUCCESS, LOGOUT_ERROR, LOGOUT_PENDING, LOGOUT_SUCCESS,
    REGISTER_ERROR,
    REGISTER_PENDING,
    REGISTER_SUCCESS
} from './auth.types';
import {Dispatch} from "redux";
import {axiosInstance} from "../../axios/axiosConfig";
import {AxiosError, AxiosResponse} from 'axios';


function logoutPending() {
    return {
        type: LOGOUT_PENDING
    }
}

function logoutSuccess() {
    return {
        type: LOGOUT_SUCCESS,
    }
}

function logoutError(error: AxiosError) {
    return {
        type: LOGOUT_ERROR,
        error
    }
}

function loginPending() {
    return {
        type: LOGIN_PENDING
    }
}

function loginSuccess(token: string) {
    return {
        type: LOGIN_SUCCESS,
        token
    }
}

function loginError(error: AxiosError) {
    return {
        type: LOGIN_ERROR,
        error
    }
}

function registerPending() {
    return {
        type: REGISTER_PENDING
    }
}

function registerError(error: AxiosError) {
    return {
        type: REGISTER_ERROR,
        error
    }
}

function registerSuccess(data: AxiosResponse) {
    return {
        type: REGISTER_SUCCESS,
        data
    }
}

function verifyPending() {
    return {
        type: REGISTER_PENDING
    }
}

function verifyError(error: AxiosError) {
    return {
        type: REGISTER_ERROR,
        error
    }
}

function verifySuccess(data: AxiosResponse) {
    return {
        type: REGISTER_SUCCESS,
        data
    }
}

export const verify = (token: string) => {
    return (dispatch: Dispatch) => {
        dispatch(verifyPending());
        axiosInstance.post('/verify', {token})
            .then(res => {
                setTimeout(() => {
                    dispatch(verifySuccess(res.data))
                }, 5000)
            })
            .catch(err => {
                console.log(err)
                dispatch(verifyError(err));
            })
    }
}

export const register = (credentials: { username: string, password: string, email: string }) => {
    return (dispatch: Dispatch) => {
        dispatch(registerPending());
        axiosInstance.post('/signup', credentials)
            .then(res => {
                dispatch(registerSuccess(res.data))
            })
            .catch(err => {
                console.log(err)
                dispatch(registerError(err));
            })
    }
}

export const logout = () => {
    return (dispatch: Dispatch) => {
        dispatch(logoutPending());
        axiosInstance.get('/logout')
            .then(res => {
                dispatch(logoutSuccess());
            })
            .catch(error => {
                console.log(error)
                dispatch(logoutError(error));
            })
    }
}

export const login = (credentials: { email: string, password: string }) => {

    return (dispatch: Dispatch) => {
        dispatch(loginPending());
        axiosInstance.post('/authenticate', credentials)
            .then(res => {
                console.log("LoginResponse", res)
                dispatch(loginSuccess(res.data))
            })
            .catch((error:AxiosError) => {
                console.log(error.toJSON())

                dispatch(loginError(error));
            })
    }
}