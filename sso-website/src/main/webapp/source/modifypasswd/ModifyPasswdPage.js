import React, { Component } from "react";
import axios from "axios";

export default class ModifyPasswdPage extends Component {

    constructor(props) {
        super(props);

        this.state = {
            oldPasswd: '',
            newPasswd: '',
            confirmNewPasswd: ''
        };
    }

    oldPasswdOnChange(e) {
        this.setState({ oldPasswd: e.target.value });
    }

    newPasswdOnChange(e) {
        this.setState({ newPasswd: e.target.value });
    }

    confirmNewPasswdOnChange(e) {
        this.setState({ confirmNewPasswd: e.target.value });
    }

    // Only user admin can modify password
    confirmOnClick() {
        const oldPasswd = this.state.oldPasswd;
        const newPasswd = this.state.newPasswd;
        const confirmPasswd = this.state.confirmNewPasswd;

        if (newPasswd !== confirmPasswd) {
            alert('两次输入的新密码不一致!');
        }
        if (oldPasswd === newPasswd || oldPasswd === confirmPasswd) {
            alert('新旧密码不能相同!');
        }

        axios.post('/rest/ssowebsite/modifypasswd',
            {
                userName: 'admin',
                newPasswd: newPasswd
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
                        <td>{'初始密码: '}</td>
                        <td>
                            <input type={'password'} value={this.state.oldPasswd} placeholder={'初始密码'} onChange={(e) => this.oldPasswdOnChange(e)}/>
                        </td>
                    </tr>
                    <tr>
                        <td>{'新密码: '}</td>
                        <td>
                            <input type={'password'} value={this.state.newPasswd} placeholder={'新密码'} onChange={(e) => this.newPasswdOnChange(e)}/>
                        </td>
                    </tr>
                    <tr>
                        <td>{'确认新密码: '}</td>
                        <td>
                            <input type={'password'} value={this.state.confirmNewPasswd} placeholder={'确认新密码'} onChange={(e) => this.confirmNewPasswdOnChange(e)}/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <button onClick={()=>this.confirmOnClick()} style={{ width: '100px', height: '30px', marginLeft: '92px' }}>{'确认'}</button>
            </div>
        );
    }

}
