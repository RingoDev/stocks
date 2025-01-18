import {AxiosError} from "axios";
import {logout} from "./authentication/auth.actions";
import {Dispatch} from "redux";

export function checkErrorResponse(err: AxiosError,dispatch:Dispatch) {
    if (err.response) {
        if (err.response.status === 401) {
            // @ts-ignore
            dispatch(logout())
            console.log('Error Response:',err.response)
        }
    }
}