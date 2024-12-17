import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../homepage.css";

const ITEMS_PER_PAGE = 10;

const HomePage = () => {
  const navigate = useNavigate();
  const [activeCategory, setActiveCategory] = useState("All");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1); // Track the total number of pages
  const [openCategories, setOpenCategories] = useState({});
  const [products, setProducts] = useState([]);
  const [categoryTree, setCategoryTree] = useState({}); // Start as empty, will be fetched from backend

  // Function to fetch all products (for default category)
  const fetchAllProducts = async (page = 1) => {
    try {
      const response = await fetch(
        `http://localhost:8081/api/1000/shop/products?page=${page - 1}&size=10`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) throw new Error("Error fetching all products");

      const data = await response.json();
      setProducts(data.content);
      setTotalPages(Math.ceil(data.totalItems / ITEMS_PER_PAGE)); // Adjust based on the totalItems value in your API
    } catch (error) {
      console.error("Error fetching all products:", error);
    }
  };

  // Function to fetch products for a specific category
  const fetchCategoryProducts = async (category, page = 1) => {
    try {
      const response = await fetch(
        `http://localhost:8081/api/products/bycategory?category=${category}&page=${page - 1}&size=10`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) throw new Error(`Error fetching products for category ${category}`);

      const data = await response.json();
      setProducts(data.content);
      setTotalPages(Math.ceil(data.totalItems / ITEMS_PER_PAGE));
    } catch (error) {
      console.error(`Error fetching products for category ${category}:`, error);
    }
  };

  // Function to fetch categories and subcategories from the backend
  const fetchCategoryTree = async () => {
    try {
      const response = await fetch("http://localhost:8081/api/7/categories/");
      if (!response.ok) throw new Error("Failed to fetch category tree");

      const categoryTreeData = await response.json();
      setCategoryTree(categoryTreeData);
      setActiveCategory("All");
    } catch (error) {
      console.error("Error fetching category tree:", error);
    }
  };

  useEffect(() => {
    // Fetch categories and category tree on initial render
    fetchCategoryTree();
  }, []);

  useEffect(() => {
    // Fetch products based on the active category
    if (activeCategory === "All") {
      fetchAllProducts(currentPage); // Fetch all products if category is "All"
    } else {
      fetchCategoryProducts(activeCategory.toLowerCase().replace(" ", ""), currentPage); // Fetch category products
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
    setCurrentPage(1); // Reset to page 1 when category changes
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div className="homepage">
      {/* Sidebar for Categories */}
      <div className="sidebar">
        <h3>Categories</h3>
        {Object.keys(categoryTree).length === 0 ? (
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
      </div>

      {/* Product Grid */}
      <div className="content">
        <div className="product-grid">
          {products.map((product) => (
            <div
              key={product.productID}
              className="product-card"
              onClick={() => navigate(`ProductPage/${product.productID}`)}
            >
              {/* Display image */}
              <img
                src={product.productImage}
                alt="productImage"
                className="product-image"
              />
              <div className="product-details">
                {/* Display price */}
                <p className="price">{product.price}</p>
                {/* Display product name */}
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

export default HomePage;
