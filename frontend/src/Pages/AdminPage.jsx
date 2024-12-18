
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../homepage.css";
import "../adminpage.css"; // Styling file


const ITEMS_PER_PAGE = 10;

const AdminPage = () => {
  const navigate = useNavigate();
  const [activeCategory, setActiveCategory] = useState("All");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [openCategories, setOpenCategories] = useState({});
  const [products, setProducts] = useState([]);
  const [categoryTree, setCategoryTree] = useState({}); // Category structure from API
  const [isLoadingCategories, setIsLoadingCategories] = useState(true);
  const [showCategoryFormSwitch, setShowCategoryFormSwitch] = useState(false);
  const [categories, setCategories] = useState([]);
  const [subcategoryName, setSubcategoryName] = useState("");
  const [subcategoryDescription, setSubcategoryDescription] = useState("");
  const [parentId, setParentId] = useState(""); // ID of the parent category
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");


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
  // Function to fetch all products
  const fetchAllProducts = async (page = 1) => {
    const productsFetched = await fetch(`http://localhost:8081/api/1000/shop/products?page=${page-1}&size=20`, {
      method:'GET',
      headers:{
        'Content-Type': 'application/json',
        // 'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
      }
    })
    .then(response=>response.status==200 || response.status==201?(()=>{return response.json()})():(()=>{throw Error("Error fetching all products")})())
    .then(data=>{
      console.log(data.content)
      setProducts(data.content);
      setTotalPages(Math.ceil(data.totalItems / ITEMS_PER_PAGE));
    })
  };

  // Function to fetch products for a specific category
  const fetchCategoryProducts = async (category, page = 1) => {
    try {
      const response = await fetch(
        `http://localhost:8081/api/7/categories/${category}?page=${page - 1}&size=${ITEMS_PER_PAGE}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (!response.ok) throw new Error(`Error fetching products for category ${category}`);

      const data = await response.json();

      // Extract products and handle subcategories
      const products = data.products || [];
      setProducts(products);

      // Calculate total pages based on total items (assuming `data.totalItems` is provided)
      setTotalPages(Math.ceil((data.totalItems || products.length) / ITEMS_PER_PAGE));
    } catch (error) {
      console.error(`Error fetching products for category ${category}:`, error);
    }
  };


  // Function to fetch categories from the API
  const fetchCategories = async () => {
    try {
      setIsLoadingCategories(true);
      const response = await fetch("http://localhost:8081/api/7/categories/");
      if (!response.ok) throw new Error("Failed to fetch categories");

      const data = await response.json();

      // Extract the category tree from the response
      const tree = data.tree || {}; // Assuming `tree` is the key containing the category tree
      setCategoryTree(tree);

      setActiveCategory("All"); // Default to "All" products
    } catch (error) {
      console.error("Error fetching category data:", error);
    } finally {
      setIsLoadingCategories(false);
    }
  };


  useEffect(() => {
    // Fetch categories on initial render
    fetchCategories();
    // Fetch products based on the active category
    if (activeCategory === "All") {
      fetchAllProducts(currentPage);
    } else {
      fetchCategoryProducts(activeCategory, currentPage);
    }
  }, [currentPage, activeCategory]);

  const toggleCategory = (mainCategory) => {
    setOpenCategories((prev) => ({
      ...prev,
      [mainCategory]: !prev[mainCategory],
    }));
  };

  const handleCategoryClick = (category) => {
    setActiveCategory(category);
    setCurrentPage(1); // Reset to the first page when the category changes
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div className="homepage">
      {/* Sidebar for Categories */}
      {showCategoryFormSwitch?
      <div className="sidebar">
           <div className="subcategory-form">
           <h3 style={{display:"flex", alignItems:"center", justifyContent:"space-around"}}>Add New Subcategory
        <div className="addButton" onClick={()=>setShowCategoryFormSwitch(prev=>!prev)}>-</div>
        </h3>
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
      </div>
      :<div className="sidebar">
        <h3 style={{display:"flex", alignItems:"center", justifyContent:"space-around"}}>Categories
        <div className="addButton" onClick={()=>setShowCategoryFormSwitch(prev=>!prev)}>+</div>
        </h3>
        {isLoadingCategories ? (
          <p>Loading categories...</p>
        ) : (
          Object.keys(categoryTree).map((mainCategory) => (
            <div key={mainCategory} className="category-group">
              <h4
                className="collapsible-header"
                onClick={() => toggleCategory(mainCategory)}
              >
                {mainCategory} {openCategories[mainCategory] ? "-" : "+"}
              </h4>
              {openCategories[mainCategory] && (
                <ul>
                  {categoryTree[mainCategory].map((subcategory) => (
                    <li
                      key={subcategory}
                      className={`category ${
                        activeCategory === subcategory ? "active" : ""
                      }`}
                      onClick={() => handleCategoryClick(subcategory)}
                    >
                      {subcategory}
                    </li>
                  ))}
                </ul>
              )}
            </div>
          ))
        )}
        <li
          className={`category ${activeCategory === "All" ? "active" : ""}`}
          onClick={() => handleCategoryClick("All")}
        >
          Show All
        </li>
      </div>}

      {/* Product Grid */}
      <div className="content">
        <div className="product-grid">
          {products.map((product) => (
            <div key={product.productID} className="product-card" onClick={()=>navigate(`/userSession/product/admin/${product.productID}`)}>
                {/* Display image */}
                <img src={product.productImage} alt="productImage" className="product-image" />
              <div className="product-details">
                <p className="price">{product.price}</p>
                <p>{product.description}</p>
              </div>
            </div>
          ))}
        </div>

        {/* Pagination */}
        <div className="pagination">
          {Array.from({ length: totalPages }, (_, i) => (
            <button
              key={i + 1}
              className={`page-btn ${currentPage === i + 1 ? "active" : ""}`}
              onClick={() => handlePageChange(i + 1)}
            >
              {i + 1}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AdminPage;