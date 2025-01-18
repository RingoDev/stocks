import React from 'react';
import {RootState} from "../redux/rootReducer";
import {Dispatch} from "redux";
import {verify} from "../redux/authentication/auth.actions";
import {connect, ConnectedProps} from "react-redux";


class Verification extends React.Component<PropsFromRedux, { }> {




    componentDidMount() {
        const token = window.location.search.slice(1).replace("token=","");
        this.props.onVerify(token);
    }

    render() {


        return (
            <>
                <h1>You will be redirected shortly</h1>
            </>
        )
    }
}

const mapState = (state: RootState) => {
    return {
    }
}

const mapDispatch = (dispatch: Dispatch) => {

    return {
        // @ts-ignore
        onVerify: (token:string) => dispatch(verify(token)),
    }
}

const connector = connect(
    mapState,
    mapDispatch
)

type PropsFromRedux = ConnectedProps<typeof connector>

export default connector(Verification)




