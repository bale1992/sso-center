import React, { Component } from "react";
import axios from "axios";

export default class AddUserPage extends Component {

    constructor(props) {
        super(props);

        this.state = {
            userName: '',
            passwd: '',
            role: ''
        };
    }

    userNameOnChange(e) {
        this.setState({userName: e.target.value});
    }

    passwdOnChange(e) {
        this.setState({passwd: e.target.value});
    }

    roleOnChange(e) {
        this.setState({passwd: e.target.role});
    }

    confirmOnClick() {
        axios.post('/rest/ssowebsite/adduser',
            {
                userName: this.state.userName,
                passwd: this.state.passwd,
                role: this.state.role
            },
            {
                'Content-Type': 'application/json'
            }
        ).then(function (response) {
            console.log(response);
        }).catch(function (error) {
            console.log(error);
        })
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
                    <tr>
                        <td>{'角色: '}</td>
                        <td>
                            <input value={this.state.role} placeholder={'角色'} onChange={(e) => this.roleOnChange(e)}/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <button onClick={()=>this.confirmOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'确认'}</button>
            </div>
        );
    }
}
