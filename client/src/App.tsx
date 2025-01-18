import React from 'react';
import {BrowserRouter as Router,  Switch, Route} from "react-router-dom";
import PrivateRoute from "./routing/GuardedRoute";

import Auth from "./auth/Auth";
import Userspace from "./app/Userspace";
import NotFound from "./NotFound";
import Verification from "./auth/Verification";


type AppProps = {}

type AppState = {}

//todo create route and component verify to verify mail address

class App extends React.Component<AppProps, AppState> {
    render() {
        return (
            <>
                <Router>
                    <Switch>
                        <PrivateRoute exact={true} path={"/auth"} component={Auth} opposite={true}/>
                        <PrivateRoute exact={true} path={"/login"} component={Auth} opposite={true}/>
                        <PrivateRoute exact={true} path={"/register"} component={Auth} opposite={true}/>
                        <PrivateRoute exact={true} path={"/"} component={Userspace}/>
                        <PrivateRoute exact={true} path={"/userspace"} component={Userspace}/>
                        <PrivateRoute exact={true} path={"/dashboard"} component={Userspace}/>
                        <PrivateRoute exact = {true} path={"/logout"} component={Userspace}/>
                        <PrivateRoute exact = {true} path={"/profile"} component={Userspace}/>
                        <Route exact={true} path={"/verify"} component={Verification}/>
                        <Route component={NotFound}/>
                    </Switch>
                </Router>
            </>
        )
    }
}

export default App;
