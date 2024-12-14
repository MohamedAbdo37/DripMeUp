import { ToastContainer } from "react-toastify";
import {Route, createRoutesFromElements, RouterProvider, createBrowserRouter} from 'react-router-dom';
import LoginPage from './Pages/LoginPage';
import AdminLoginPage from './Pages/AdminLoginPage';
import EmptyPage from './Pages/EmptyPage';
import SignupPage from './Pages/SignupPage';
import ForgotPasswordPage from './Pages/ForgotPasswordPage';
import UserProfilePage from './Pages/UserProfilePage';
import AdminProfilePage from './Pages/AdminProfilePage';
import WelcomePage from './Pages/WelcomePage';
import HomePage from './Pages/HomePage';
import "react-toastify/dist/ReactToastify.css";


function App() {
  
  const router = createBrowserRouter(
    createRoutesFromElements(
      <>
        {/* <Route index element={<HomePage/>}/>
        <Route path='/blogs' element={<BlogsCards submit={postBlog} delBlog={delBlog} databaseSrc={databaseSrc}/>}/> */}
        <Route Route path='/' element={<WelcomePage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/login' element={<LoginPage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/admin/login' element={<AdminLoginPage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/homepage' element={<HomePage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/profile' element={<UserProfilePage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/admin/profile' element={<AdminProfilePage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/signup' element={<SignupPage/>} errorElement= {<EmptyPage/>}/>
        <Route path='/forgotpassword' element={<ForgotPasswordPage/>} errorElement= {<EmptyPage/>}/>
        <Route path='*' element={<EmptyPage/>}/>
      </>
  ));
  return (
    <>
      <RouterProvider router={router}/>
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
      />
    </>
  )
}

export default App
