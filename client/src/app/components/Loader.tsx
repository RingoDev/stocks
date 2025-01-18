import React, {Component} from 'react';

interface LoaderProps {
    size?: number,
    className?: string
}

export default class Loader extends Component<LoaderProps, any> {


    render() {
        let size = 1;
        if (this.props.size !== undefined) size = this.props.size;

        const divStyle = {
            width: (size * 12.8).toString() + "vw",
            height: (size * 12.8).toString() + "vw",
            margin: "3.2vw",
            border: "1.6vw solid",
            borderColor: "#fff transparent transparent transparent"
        }
        const ldsStyle = {
            width: (size * 16).toString() + "vw",
            height: (size * 16).toString() + "vw",
        }
        return (
            <>
                <div className={this.props.className + ' lds-ring'} style={ldsStyle}>
                    <div style={divStyle}/>
                    <div style={divStyle}/>
                    <div style={divStyle}/>
                    <div style={divStyle}/>
                </div>
            </>
        )
    }
}