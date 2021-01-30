import React, { Component } from "react";

export default class LoginPage extends Component {

    constructor(props) {
        super(props);

        this.state = {
            userName: '',
            passwd: ''
        };
    }

    userNameOnChange(e) {
        this.setState({userName: e.target.value});
    }

    passwdOnChange(e) {
        this.setState({passwd: e.target.value});
    }

    logInOnClick() {
        console.log(this.state.userName);
        console.log(this.state.passwd);
    }

    render() {
        return (
            <div>
                <table>
                    <tbody>
                    <tr>
                        <td>{'用户名: '}</td>
                        <td>
                            <input value={this.state.userName} placeholder={'用户名'} onChange={(e) => this.userNameOnChange(e)}/>
                        </td>
                    </tr>
                    <tr>
                        <td>{'密码: '}</td>
                        <td>
                            <input type={'password'} value={this.state.passwd} placeholder={'密码'} onChange={(e) => this.passwdOnChange(e)}/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <button onClick={()=>this.logInOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'登陆'}</button>
            </div>
        );
    }
}
