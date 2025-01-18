import React from "react";
import {Route, Redirect, } from 'react-router-dom'
import {RouteProps, } from 'react-router';
import {connect} from "react-redux";
import {RootState} from "../redux/rootReducer";
import {getLoggedIn} from "../redux/authentication/auth.reducer";


// type GuardedRouteProps = RouteProps & { auth: boolean, redirect: string }
//
//
// const GuardedRoute = ({component: Component, auth, redirect, ...rest}: GuardedRouteProps) => {
//
//     return (
//         <Route {...rest} render={props => (
//             auth
//                 // @ts-ignore
//                 ? (<Component {...props}/>)
//                 : (<Redirect to={{pathname: redirect, state: {from: props.location}}}/>)
//         )}/>
//     );
// }
// export default GuardedRoute;


interface PrivateRouteProps extends RouteProps {
    loggedIn: boolean,
    opposite?: boolean
}

interface PrivateRouteState {
}

class PrivateRoute extends React.Component<PrivateRouteProps, PrivateRouteState> {
    render() {
        let doOpposite = false;
        if (this.props.opposite !== undefined) doOpposite = this.props.opposite;
        if (!doOpposite) {
            if (this.props.loggedIn) return <Route {...this.props} />;
            else {
                console.log("redirecting someone to /login")
                return <Redirect to="/login"/>;
            }
        } else {
            if (!this.props.loggedIn) return <Route {...this.props} />;
            else return <Redirect to="/userspace"/>;
        }
    }
}



const mapStateToProps = (state: RootState) => ({
    loggedIn: getLoggedIn(state.auth)
});

const connector = connect(mapStateToProps)(PrivateRoute)

export default connector



