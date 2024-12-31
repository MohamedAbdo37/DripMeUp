import { ToastContainer } from "react-toastify";
import { Route, createRoutesFromElements, RouterProvider, createBrowserRouter } from 'react-router-dom';
import LoginPage from './Pages/LoginPage';
import AdminLoginPage from './Pages/AdminLoginPage';
import EmptyPage from './Pages/EmptyPage';
import SignupPage from './Pages/SignupPage';
import ForgotPasswordPage from './Pages/ForgotPasswordPage';
import UserProfilePage from './Pages/UserProfilePage';
import WelcomePage from './Pages/WelcomePage';
import HomePage from './Pages/HomePage';
import ProductPage from './Pages/ProductPage';
import UserNavBar from './Layouts/UserNavBar';
import AdminNavBar from './Layouts/AdminNavBar';
import FavouritesPage from './Pages/FavouritesPage';
import AddAdminPage from './Pages/AddAdminPage';
import CartPage from './Pages/CartPage';
import SettingsPage from './Pages/SettingsPage';
import AdminPage from "./Pages/AdminPage";
import OrdersPage from './Pages/OrdersPage';
import OrderDetailsPage from './Pages/OrderDetailsPage';
import UserOrdersPage from './Pages/UserOrdersPage';
import UserOrderDetailsPage from './Pages/UserOrderDetailsPage';
import "react-toastify/dist/ReactToastify.css";
import Modal from 'react-modal';

function App() {
  const router = createBrowserRouter(
    createRoutesFromElements(
      <>
        <Route path='/' element={<WelcomePage />} errorElement={<EmptyPage />} />
        <Route path='/login' element={<LoginPage />} errorElement={<EmptyPage />} />
        <Route path='/admin/login' element={<AdminLoginPage />} errorElement={<EmptyPage />} />
        <Route path='/signup' element={<SignupPage />} errorElement={<EmptyPage />} />
        <Route path='/forgotpassword' element={<ForgotPasswordPage />} errorElement={<EmptyPage />} />
        <Route path='userSession' element={<UserNavBar />} errorElement={<EmptyPage />}>
          <Route index element={<HomePage />} errorElement={<EmptyPage />} />
          <Route path='profile' element={<UserProfilePage />} errorElement={<EmptyPage />} />
          <Route path='product/:person/:productID' element={<ProductPage />} errorElement={<EmptyPage />} />
          <Route path='settings' element={<SettingsPage />} errorElement={<EmptyPage />} />
          <Route path='cart' element={<CartPage />} errorElement={<EmptyPage />} />
          <Route path='favourites' element={<FavouritesPage />} errorElement={<EmptyPage />} />
          <Route path='my-orders' element={<UserOrdersPage />} errorElement={<EmptyPage />} />
          <Route path='my-orders/:orderId' element={<UserOrderDetailsPage />} errorElement={<EmptyPage />} />
        </Route>
        <Route path='adminSession' element={<AdminNavBar />} errorElement={<EmptyPage />}>
          <Route index element={<AdminPage />} errorElement={<EmptyPage />} />
          <Route path='orders' element={<OrdersPage />} errorElement={<EmptyPage />} />
          <Route path='orders/:orderId' element={<OrderDetailsPage />} errorElement={<EmptyPage />} />
          <Route path='product/:person/:productID' element={<ProductPage />} errorElement={<EmptyPage />} />
          <Route path='addAdmin' element={<AddAdminPage />} errorElement={<EmptyPage />} />
          <Route path='settings' element={<SettingsPage />} errorElement={<EmptyPage />} />
        </Route>
        <Route path='*' element={<EmptyPage />} />
      </>
    )
  );

  return (
    <>
      <RouterProvider router={router} />
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
  );
}

export default App;
Modal.setAppElement('#root');