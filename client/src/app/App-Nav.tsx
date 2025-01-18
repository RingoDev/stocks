import React from "react";
import {Navbar, NavbarBrand} from 'reactstrap';
import {NavLink} from 'react-router-dom';


export default class AppNav extends React.Component {


    render() {
        return (
            <>
                <Navbar expand={true} light={true} className="static-top">
                    <NavbarBrand href={"/"} className={"mr-auto"}>RingoDev Stocks</NavbarBrand>

                    <ul className="navbar-nav ml-auto">
                        <NavLink className={"nav-link"} to={"/logout"}>Log out</NavLink>
                        <NavLink className={"nav-link"} to={"/profile"}>Profile</NavLink>
                        <NavLink className={"nav-link"} to={"/dashboard"}>Dashboard</NavLink>
                    </ul>
                </Navbar>
            </>
        )
    }
}