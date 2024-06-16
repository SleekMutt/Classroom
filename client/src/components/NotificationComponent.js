import * as React from 'react';
import IconButton from '@mui/material/IconButton';
import Menu from '@mui/material/Menu';
import Badge from '@mui/material/Badge';
import MailIcon from '@mui/icons-material/Mail';
import { List, ListItem, ListItemText, Button, Divider, MenuItem } from "@mui/material"
import CloseIcon from '@mui/icons-material/Close';
import SockJS from 'sockjs-client';
import { useEffect, useState } from 'react'
import { Stomp } from '@stomp/stompjs';
import { axiosAPI } from '../api/axiosClient';
import { useNavigate } from 'react-router';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import 'react-notifications/lib/notifications.css';


export default function LongMenu() {
    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);
    const [notifications, setNotifications] = useState([]);

    const navigate = useNavigate();

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };
    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws');
        const stompClient = Stomp.over(socket);
        axiosAPI.get("/user/notification-by-authorized", {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          })      
          .then(response => {
            setNotifications((prevNotifications) => [...prevNotifications, ...response.data]);

          })
          .catch(error => {
            navigate('/error', {
              state: {
                code: error.message,
                message: error.response.data.messages
              }
            })
          })


        stompClient.connect({
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        }, (frame) => {
            stompClient.subscribe('/user/topic/notifications', (message) => {
                NotificationManager.info("New notification", "", 3000)
                const newNotifications = JSON.parse(message.body);
                setNotifications((prevNotifications) => [...prevNotifications, newNotifications]);
            });

        });


        return () => {
            if (stompClient) {
                stompClient.disconnect();
            }
        };
    }, []);

    return (
        
        <div>            
        <IconButton
                aria-label="more"
                id="long-button"
                aria-controls={open ? 'long-menu' : undefined}
                aria-expanded={open ? 'true' : undefined}
                aria-haspopup="true"
                onClick={handleClick}
            >
                <Badge color="secondary" badgeContent={notifications.length}>
                    <MailIcon />
                </Badge>
            </IconButton>
            <Menu

                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                style={{ maxHeight: "400px" }}

            >

                {notifications.sort((a, b) => new Date(b.sentDate) - new Date(a.sentDate)).map((notification) => (

                    <MenuItem
                    >
                        <div style={{
                            maxWidth: '400px',
                            minWidth: '400px',
                            whiteSpace: 'normal',
                            wordWrap: 'break-word'
                        }}>
                            <div style={{ display: 'flex', flexDirection: 'column' }}>
                                <span>{notification.message}</span>
                                <span style={{ alignSelf: 'flex-end' }}>
                                    {new Date(notification.sentDate).toLocaleString('en-US', {
                                        month: 'short',
                                        day: '2-digit',
                                        hour: '2-digit',
                                        minute: '2-digit',
                                    })}
                                </span>
                            </div>
                        </div>

                        <IconButton aria-label="Example">
                            <CloseIcon></CloseIcon>
                        </IconButton>

                        <Divider component="li" />

                    </MenuItem>))}

            </Menu>
            <NotificationContainer/>
        </div>
    );
}
