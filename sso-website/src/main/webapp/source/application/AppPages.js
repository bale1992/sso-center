import React, { Component } from "react";
import axios from "axios";

export default class AppPages extends Component {

    oneOnClick() {
        axios.post('/rest/ssoservice/producer/one',
            null,
            {
                'Content-Type': 'application/json'
            }
        ).catch(function (error) {
            console.log(error);
        })
    }

    twoOnClick() {
        axios.post('/rest/ssoservice/producer/two',
            null,
            {
                'Content-Type': 'application/json'
            }
        ).catch(function (error) {
            console.log(error);
        })
    }

    ThreeOnClick() {
        axios.post('/rest/ssoservice/producer/three',
            null,
            {
                'Content-Type': 'application/json'
            }
        ).catch(function (error) {
            console.log(error);
        })
    }

    render() {
        return (
            <div>
                <button onClick={()=>this.oneOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'ONE'}</button>
                <button onClick={()=>this.twoOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'TWO'}</button>
                <button onClick={()=>this.ThreeOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'THREE'}</button>
            </div>
        );
    }
};
