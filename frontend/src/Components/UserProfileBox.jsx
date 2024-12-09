import React, { useState } from 'react';
import {useLocation} from 'react-router-dom';
import unknownPhoto from '../assets/unknown.jpg'; // Adjust the path as necessary

const UserProfileBox = () => {
  const { user } = location.state || {};
  
  const [isEditingName, setIsEditingName] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUser({ ...user, [name]: value });
  };

  const handlePhotoChange = (e) => {
    setUser({ ...user, photo: e.target.files[0] });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setIsEditingName(false);
    // Handle form submission logic here
    console.log('User data submitted:', user);
  };

  return (
    <div className="user-profile-box" style={styles.container}>
      <form onSubmit={handleSubmit} style={styles.form}>
        <div style={styles.formGroup}>
          <label style={styles.label}>Name:</label>
          {isEditingName ? (
            <input
              type="text"
              name="name"
              value={user.username}
              onChange={handleInputChange}
              style={styles.input}
            />
          ) : (
            <div style={styles.staticTextContainer}>
              <p style={styles.staticText}>{user.username}</p>
              <button
                type="button"
                onClick={() => setIsEditingName(true)}
                style={styles.editButton}
              >
                Edit
              </button>
            </div>
          )}
        </div>
        <div style={styles.formGroup}>
          <label style={styles.label}>Email:</label>
          <p style={styles.staticText}>{user.email}</p>
        </div>
        <div style={styles.formGroup}>
          <label style={styles.label}>Photo:</label>
          <input
            type="file"
            name="photo"
            onChange={handlePhotoChange}
            style={styles.input}
          />
          <div style={styles.preview}>
            <img
              src={user.photo ? URL.createObjectURL(user.photo) : unknownPhoto}
              alt="User Photo"
              style={styles.image}
            />
          </div>
        </div>
        <div style={styles.buttonGroup}>
          <button type="submit" style={styles.button}>Save</button>
        </div>
      </form>
    </div>
  );
};

const styles = {
  container: {
    maxWidth: '400px',
    margin: '0 auto',
    padding: '20px',
    border: '1px solid #ccc',
    borderRadius: '8px',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
  },
  formGroup: {
    marginBottom: '15px',
  },
  label: {
    marginBottom: '5px',
    fontWeight: 'bold',
  },
  input: {
    padding: '8px',
    fontSize: '16px',
    borderRadius: '4px',
    border: '1px solid #ccc',
  },
  staticTextContainer: {
    display: 'flex',
    alignItems: 'center',
  },
  staticText: {
    padding: '8px',
    fontSize: '16px',
    borderRadius: '4px',
    border: '1px solid #ccc',
    backgroundColor: '#f9f9f9',
    marginRight: '10px',
  },
  editButton: {
    padding: '5px 10px',
    fontSize: '12px',
    borderRadius: '4px',
    border: 'none',
    backgroundColor: '#007BFF',
    color: '#fff',
    cursor: 'pointer',
  },
  buttonGroup: {
    display: 'flex',
    justifyContent: 'center',
  },
  button: {
    padding: '10px',
    fontSize: '16px',
    borderRadius: '4px',
    border: 'none',
    backgroundColor: '#007BFF',
    color: '#fff',
    cursor: 'pointer',
  },
  preview: {
    marginTop: '20px',
    textAlign: 'center',
  },
  image: {
    width: '100px',
    height: '100px',
    borderRadius: '50%',
  },
};

export default UserProfileBox;