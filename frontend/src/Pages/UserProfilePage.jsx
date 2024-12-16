import { useNavigate, useOutletContext } from "react-router-dom";
import UserProfileBox from "../Components/UserProfileBox";
import { useEffect } from "react";

const UserProfilePage = () => {
    const navigate = useNavigate();
    return (
        <div className="profilePage">
            <UserProfileBox/>
        </div>
    );
};

export default UserProfilePage;