import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import unknownPhoto from '../assets/unknown.jpg'; // Adjust the path as necessary
import UploadPhoto from './UploadPhoto';
import ChangePassword from './ChangePassword';
import SignupPage from '../Pages/SignupPage';
import { useNavigate } from 'react-router-dom';
import edit from '../assets/edit.png'
import '../style.css';
const UserProfileBox = () => {
  const [user, setUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isUploadPhotoOpen, setIsUploadPhotoOpen] = useState(false);
  const [isChangePasswordOpen, setIsChangePasswordOpen] = useState(false);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    gender: '',
    phoneNumber: ''
  });
  const [profilePhoto, setProfilePhoto] = useState(null);
  const navigate = useNavigate();
  useEffect(() => {
    const fetchUserData = async () => {
      const token = localStorage.getItem('drip_me_up_jwt');
      console.log(token.length)
      try {
        const response = await fetch('http://localhost:8081/users/', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.ok) {
          const userData = await response.json();
          setUser(userData);
          setFormData({
            username: userData.username,
            email: userData.email,
            gender: userData.gender,
            phoneNumber: userData.phoneNumber || ''
          });
          setProfilePhoto(userData.profilePhoto || unknownPhoto);
        } else if (response.status === 401) {
          console.error('Unauthorized');
        }  else if (response.status === 403) {
          console.error('Forbidden');
        } else {
          console.error('Failed to fetch user data');
        }
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    };

    fetchUserData();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleEditClick = () => {
    setIsEditing(true);
  };

  const handleCancelClick = () => {
    setIsEditing(false);
    setFormData({
      username: user.username,
      email: user.email,
      gender: user.gender,
      phoneNumber: user.phoneNumber || ''
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('drip_me_up_jwt');
      const response = await fetch('http://localhost:8081/users/', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        const result = await response.json();
        console.log(result.message);
        setUser({ ...user, ...formData });
        setIsEditing(false);
      } else if (response.status === 400) {
        const error = await response.json();
        console.error('Invalid input data:', error.error);
      } else if (response.status === 401) {
        console.error('Unauthorized');
      } else {
        console.error('Failed to update user data');
      }
    } catch (error) {
      console.error('Error updating user data:', error);
    }
  };

  const handleUploadPhotoOpen = () => {
    setIsUploadPhotoOpen(true);
  };

  const handleUploadPhotoClose = () => {
    setIsUploadPhotoOpen(false);
  };

  const handleChangePasswordOpen = () => {
    setIsChangePasswordOpen(true);
  };

  const handleChangePasswordClose = () => {
    setIsChangePasswordOpen(false);
  };

  const handlePhotoUpload = (newPhoto) => {
    setProfilePhoto(newPhoto);
    setUser({ ...user, profilePhoto: newPhoto });
  };

  const handleLogout = () =>{
    localStorage.removeItem('drip_me_up_jwt');
    navigate('/');
  };

  if (!user) {
    return(
      <center>
        <h1>You are a Guest</h1>
        <button className='backButton' onClick={()=>navigate('/signup')}>Signup First</button>
      </center>);
  }

  return (
    <ProfileContainer>
      <ProfileImage src={profilePhoto || unknownPhoto} alt="Profile" />
      {isEditing ? (
        <form onSubmit={handleSubmit}>
          <ProfileDetails>
            <ProfileInput
              type="text"
              name="username"
              value={formData.username}
              onChange={handleInputChange}
              placeholder="Username"
              required
            />
            <ProfileInput
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              placeholder="Email"
              required
            />
            <ProfileSelect
              name="gender"
              value={formData.gender}
              onChange={handleInputChange}
              required
            >
              <option value="">Select Gender</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="UNKNOWN">Prefer not to say</option>
            </ProfileSelect>
            <ProfileInput
              type="text"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleInputChange}
              placeholder="Phone Number"
            />
            <ButtonContainer>
              <ProfileButton type="submit">Save</ProfileButton>
              <ProfileButton type="button" onClick={handleCancelClick}>
                Cancel
              </ProfileButton>
              {/* <ProfileButton type="button" onClick={handleUploadPhotoOpen}>
                <img src={edit} style={{scale:"0.6"}}/>
              </ProfileButton> */}
              <img src={edit} style={{width: "2rem", height: "2rem", cursor: "pointer", transform: "translate(200%, -850%)", margin: "0"}} onClick={handleUploadPhotoOpen}/>
              <ProfileButton type="button" onClick={handleChangePasswordOpen}>
                Change Password
              </ProfileButton>
            </ButtonContainer>
          </ProfileDetails>
        </form>
      ) : (
        <ProfileDetails>
          <ProfileName>{user.username}</ProfileName>
          <ProfileInfo>Email: {user.email}</ProfileInfo>
          <ProfileInfo>Gender: {user.gender}</ProfileInfo>
          <ProfileInfo>Phone Number: {user.phoneNumber}</ProfileInfo>
          <ProfileButton onClick={handleEditClick}>Edit Profile</ProfileButton>
          <ProfileButton onClick={handleLogout}>Log out</ProfileButton>
        </ProfileDetails>
      )}
      {isUploadPhotoOpen && (
        <UploadPhoto onClose={handleUploadPhotoClose} onUpload={handlePhotoUpload} />
      )}
      {isChangePasswordOpen && (
        <ChangePassword onClose={handleChangePasswordClose} />
      )}
    </ProfileContainer>
  );
};

export default UserProfileBox;

// Styled components
const ProfileContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  margin: 3rem auto;

`;

const ProfileImage = styled.img`
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 20px;
`;

const ProfileDetails = styled.div`
  text-align: center;
`;

const ProfileName = styled.h1`
  font-size: 24px;
  margin-bottom: 10px;
`;

const ProfileInfo = styled.p`
  font-size: 16px;
  color: #555;
  margin: 5px 0;
`;

const ProfileInput = styled.input`
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const ProfileSelect = styled.select`
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const ProfileButton = styled.button`
  padding: 1rem;
  margin: 10px;
  border: none;
  border-radius: 20px;
  background-color: #5bb4c1;
  color: white;
  cursor: pointer;
  &:hover {
    background-color: #0056b3;
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 10px;
`;