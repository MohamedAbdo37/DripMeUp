import { useState, useEffect } from 'react';
import {Route, createRoutesFromElements, RouterProvider, createBrowserRouter} from 'react-router-dom';
import LoginPage from './Pages/LoginPage';
import EmptyPage from './Pages/EmptyPage';
import SignupPage from './Pages/SignupPage';
import ForgotPasswordPage from './Pages/ForgotPasswordPage';
import WelcomePage from './Pages/WelcomePage';
import HomePage from './Pages/HomePage';

function App() {
  
  const router = createBrowserRouter(
    createRoutesFromElements(
      <>
        {/* <Route index element={<HomePage/>}/>
        <Route path='/blogs' element={<BlogsCards submit={postBlog} delBlog={delBlog} databaseSrc={databaseSrc}/>}/> */}
        <Route Route path='/' element={<WelcomePage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/login' element={<LoginPage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/homepage' element={<HomePage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/signup' element={<SignupPage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/forgotpassword' element={<ForgotPasswordPage/>} errorElement= {<EmptyPage/>}/>
        <Route path='*' element={<EmptyPage/>}/>
      </>
  ));
  return (
    <>
      <RouterProvider router={router}/>
    </>
  )
}

export default App
