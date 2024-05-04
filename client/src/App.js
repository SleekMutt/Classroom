import './App.css';
import NavigationBar from "./components/NavigationBar";
import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";
import { useEffect } from 'react';
import Login from './components/login/Login';
import ErrorComponent from './components/ErrorComponent';
import authService from './services/AuthService';
import PrivateRoute from './components/route/PrivateRoute';
import OwnedCourses from './components/courseDisplay/OwnedCourses';
import JoinedCourses from './components/courseDisplay/JoinedCourses';

function App() {
  const queryParameters = new URLSearchParams(window.location.search)

  useEffect(() => {
    const accessToken =  queryParameters.get("accessToken")
    if (accessToken) {
      authService.toSetAccesstoken(accessToken)
    }
}, []);

  return (
  <Router>
      <NavigationBar/>
      <Routes>
        <Route path="/Login" element={<Login/>}></Route>
        <Route path="/" element={<PrivateRoute>

        </PrivateRoute>}>
        </Route>
        <Route path="/owned-courses" element={<PrivateRoute>
        <OwnedCourses />
        </PrivateRoute>}>
        </Route>
        <Route path="/joined-courses" element={<PrivateRoute>
        <JoinedCourses />
        </PrivateRoute>}>
        </Route>
        <Route path="/error" element={<ErrorComponent code='404' message="Oops! The page you're looking for doesn't exist."/>}></Route>
      </Routes>
  </Router>

  )
}



export default App;

