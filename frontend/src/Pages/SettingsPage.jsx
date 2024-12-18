import { useEffect } from "react";

const SettingsPage = ()=>{
    useEffect(()=>{
        console.log("ldksjf");
    }, []);
    return(
    <>
        <center style={{fontSize: "2rem"}}>This is settings page</center>
    </>
    );
};
export default SettingsPage;