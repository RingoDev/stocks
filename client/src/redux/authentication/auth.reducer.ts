import {
    LOGIN_ERROR,
    LOGIN_PENDING,
    LOGIN_SUCCESS,
    LOGOUT_ERROR, LOGOUT_PENDING, LOGOUT_SUCCESS,
    REGISTER_ERROR,
    REGISTER_PENDING,
    REGISTER_SUCCESS, VERIFICATION_ERROR, VERIFICATION_PENDING, VERIFICATION_SUCCESS
} from './auth.types';
import {AxiosError} from 'axios';

export type INITIAL_STATE = {
    loggedIn: boolean,
    loginPending: boolean,
    loginError: AxiosError | undefined,
    registerPending: boolean,
    registerError: AxiosError | undefined,
    logoutPending: boolean,
    logoutError: AxiosError | undefined,
    verificationPending: boolean,
    verificationError: AxiosError | undefined,
}

const INITIAL_STATE:INITIAL_STATE = {
    loggedIn: !!localStorage.getItem("session"),
    loginPending: false,
    loginError: undefined,
    registerPending: false,
    registerError: undefined,
    logoutPending: false,
    logoutError: undefined,
    verificationPending: false,
    verificationError: undefined,
};

const reducer = (state = INITIAL_STATE, action: { type: string, error?: AxiosError }) => {

    switch (action.type) {

        case LOGIN_PENDING:
            return {
                ...state,
                loginPending: true,
                loginError: undefined
            };

        case LOGIN_SUCCESS:
            localStorage.setItem("session", "")
            return {
                ...state,
                loggedIn: true,
                loginPending: false,
                loginError: undefined
            };
        case LOGIN_ERROR:
            localStorage.removeItem("session")
            return {
                ...state,
                loginPending: false,
                loginError: action.error
            };
        case REGISTER_PENDING:
            return {
                ...state,
                registerPending: true,
                registerError: undefined
            };

        case REGISTER_SUCCESS:
            return {
                ...state,
                registerPending: false,
                registerError: undefined
            };
        case REGISTER_ERROR:
            return {
                ...state,
                registerPending: false,
                registerError: action.error
            };

        case VERIFICATION_PENDING:
            return {
                ...state,
                verificationPending: true,
                verificationError: undefined,
            };

        case VERIFICATION_SUCCESS:
            return {
                ...state,
                verificationPending: false,
                verificationError: undefined,
            };
        case VERIFICATION_ERROR:
            return {
                ...state,
                verificationPending: false,
                verificationError: action.error
            };

        case LOGOUT_PENDING:
            return {
                ...state,
                logoutPending: true,
                logoutError: undefined,
            };

        case LOGOUT_SUCCESS:
            return {
                ...state,
                logoutPending: false,
                logoutError: undefined,
                loggedIn: false,
            };
        case LOGOUT_ERROR:
            return {
                ...state,
                logoutError: action.error
            };


        default:
            return state;
    }

};


export const getLoggedIn = (state: typeof INITIAL_STATE) => state.loggedIn
export const getLoginError = (state: typeof INITIAL_STATE) => state.loginError

export default reducer;