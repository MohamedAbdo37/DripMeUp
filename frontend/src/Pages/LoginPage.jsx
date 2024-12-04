import LoginBox from "../Components/LoginBox"
import "../style.css"
import googleLogo from '../assets/logo.png'

const LoginPage = () =>{

    return(
        <div className="loginPage" style={{height: '100vh'}}>
            <LoginBox/>
        </div>
    );
};
export default LoginPage;