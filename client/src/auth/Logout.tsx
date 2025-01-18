import React from 'react';
import {logout} from "../redux/authentication/auth.actions";
import {connect, ConnectedProps} from "react-redux";
import {Dispatch} from "redux";
import {RootState} from "../redux/rootReducer";
import {getLoggedIn} from "../redux/authentication/auth.reducer";
import Loader from "../app/components/Loader";
import {Container} from "reactstrap";


class Logout extends React.Component<PropsFromRedux, any> {

    componentDidMount() {
        this.props.logout()
    }

    render() {

        return (
            <>
                <Container className={"align-items-center"} style={{textAlign: "center"}}>
                    <Loader className={"mx-auto mb-3"}/>
                    <p>Please wait while we are logging you out safely.</p>
                </Container>
            </>
        )
    }
}


type PropsFromRedux = ConnectedProps<typeof connector>

const mapState = (state: RootState) => {
    return {
        loggedIn: getLoggedIn(state.auth)
    }
}

const mapDispatch = (dispatch: Dispatch) => {

    return {
        // @ts-ignore
        logout: () => dispatch(logout()),
    }
}

const connector = connect(
    mapState,
    mapDispatch
)

export default connector(Logout)
