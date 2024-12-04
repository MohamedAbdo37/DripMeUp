import AdminLoginBox from "../Components/AdminLoginBox"
import "../style.css"
import googleLogo from '../assets/logo.png'

const AdminLoginPage = () =>{

    return(
        <div className="loginPage" style={{height: '100vh'}}>
            <AdminLoginBox/>
        </div>
    );
};
export default AdminLoginPage;