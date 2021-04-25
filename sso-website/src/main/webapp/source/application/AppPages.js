import React, { Component } from "react";
import axios from "axios";

export default class AppPages extends Component {

    constructor(props) {
        super(props);

        this.state = {
            consumer1: '',
            consumer2: '',
            consumer3: '',
        };
    }

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

    userConsumer1OnChange(e) {
        this.setState({consumer1: e.target.value});
    }
    consumerOneOnClick() {
        axios.post('/rest/ssoservice/consumer/one',
            {
                consumerGroup: this.state.consumer1
            },
            {
                'Content-Type': 'application/json'
            }
        ).catch(function (error) {
            console.log(error);
        })
    }

    userConsumer2OnChange(e) {
        this.setState({consumer2: e.target.value});
    }
    consumerTwoOnClick() {
        axios.post('/rest/ssoservice/consumer/two',
            {
                consumerGroup: this.state.consumer2
            },
            {
                'Content-Type': 'application/json'
            }
        ).catch(function (error) {
            console.log(error);
        })
    }

    userConsumer3OnChange(e) {
        this.setState({consumer3: e.target.value});
    }
    consumerThreeOnClick() {
        axios.post('/rest/ssoservice/consumer/three',
            {
                consumerGroup: this.state.consumer3
            },
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
                <div>
                    <div>{'生产者'}</div>
                    <button onClick={()=>this.oneOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'ONE'}</button>
                    <button onClick={()=>this.twoOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'TWO'}</button>
                    <button onClick={()=>this.ThreeOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'THREE'}</button>
                </div>
                <div>{'----------------------------------------------------------------------------------------------------------------------------------'}</div>
                <div>
                    <div>{'消费者'}</div>
                    <table>
                        <tbody>
                        <tr>
                            <td>
                                <input value={this.state.consumer1} placeholder={'消费者组'} onChange={(e) => this.userConsumer1OnChange(e)}/>
                            </td>
                            <td>
                                <button onClick={()=>this.consumerOneOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'ONE'}</button>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input value={this.state.consumer2} placeholder={'消费者组'} onChange={(e) => this.userConsumer2OnChange(e)}/>
                            </td>
                            <td>
                                <button onClick={()=>this.consumerTwoOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'TWO'}</button>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input value={this.state.consumer3} placeholder={'消费者组'} onChange={(e) => this.userConsumer3OnChange(e)}/>
                            </td>
                            <td>
                                <button onClick={()=>this.consumerThreeOnClick()} style={{ width: '100px', height: '30px', marginLeft: '60px' }}>{'THREE'}</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
};
