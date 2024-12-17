import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../adminpage.css"; // Styling file

const AdminPage = () => {
  const navigate = useNavigate();

  const [categories, setCategories] = useState([]);
  const [subcategoryName, setSubcategoryName] = useState("");
  const [subcategoryDescription, setSubcategoryDescription] = useState("");
  const [parentId, setParentId] = useState(""); // ID of the parent category
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  // Fetch categories (replace this with actual API endpoint when available)
  const fetchCategories = async () => {
    try {
      const response = await fetch("http://localhost:8081/api/7/categories/"); // Replace with actual categories API
      const data = await response.json();
      setCategories(data);
    } catch (error) {
      console.error("Error fetching categories:", error);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  // Handle creating a subcategory
  const handleCreateSubcategory = async () => {
    if (!subcategoryName || !subcategoryDescription || !parentId) {
      setErrorMessage("All fields are required to add a subcategory.");
      return;
    }

    const requestBody = {
      name: subcategoryName,
      slug: subcategoryName.toLowerCase().replace(/\s+/g, "-"),
      description: subcategoryDescription,
      parent_id: parentId,
    };

    try {
      const response = await fetch("http://localhost:8081/api/7/categories/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Cookie: "AdminID=12345", // Replace with the actual admin ID cookie
        },
        body: JSON.stringify(requestBody),
      });

      if (!response.ok) {
        throw new Error("Failed to create subcategory.");
      }

      setSuccessMessage("Subcategory created successfully!");
      setErrorMessage("");
      setSubcategoryName("");
      setSubcategoryDescription("");
      setParentId("");
      fetchCategories(); // Refresh categories
    } catch (error) {
      setErrorMessage(error.message || "Failed to create subcategory.");
      setSuccessMessage("");
    }
  };

  return (
    <div className="admin-container">
      {/* Header */}
      <div className="admin-header">
        <button
          className="profile-button"
          onClick={() =>
            navigate("/admin/profile", {
              state: { admin: { username: "", email: "", photo: "" } },
            })
          }
        >
          Go to Profile
        </button>
      </div>

      {/* Add Subcategory Form */}
      <div className="subcategory-form">
        <h3>Add New Subcategory</h3>
        <label htmlFor="subcategoryName">Subcategory Name:</label>
        <input
          type="text"
          id="subcategoryName"
          value={subcategoryName}
          onChange={(e) => setSubcategoryName(e.target.value)}
          placeholder="Enter subcategory name"
        />

        <label htmlFor="subcategoryDescription">Description:</label>
        <textarea
          id="subcategoryDescription"
          value={subcategoryDescription}
          onChange={(e) => setSubcategoryDescription(e.target.value)}
          placeholder="Enter subcategory description"
        />

        <label htmlFor="parentId">Parent Category:</label>
        <select
          id="parentId"
          value={parentId}
          onChange={(e) => setParentId(e.target.value)}
        >
          <option value="">Select Parent Category</option>
          {categories.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
            </option>
          ))}
        </select>

        <button onClick={handleCreateSubcategory}>+ Add Subcategory</button>
      </div>

      {/* Categories and Subcategories */}
      <div className="categories-list">
        <h3>Existing Categories and Subcategories</h3>
        {categories.map((category) => (
          <div key={category.id} className="category-item">
            <strong>{category.name}</strong>
            {category.subcategories?.length > 0 ? (
              <ul>
                {category.subcategories.map((subcategory) => (
                  <li key={subcategory.id}>
                    {subcategory.name} - {subcategory.description}
                  </li>
                ))}
              </ul>
            ) : (
              <p>No subcategories available</p>
            )}
          </div>
        ))}
      </div>

      {/* Feedback Messages */}
      {errorMessage && <p className="error-message">{errorMessage}</p>}
      {successMessage && <p className="success-message">{successMessage}</p>}
    </div>
  );
};

export default AdminPage;