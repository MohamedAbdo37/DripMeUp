import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../homepage.css";

const ITEMS_PER_PAGE = 10;

const HomePage = () => {
  const navigate = useNavigate();
  const [activeCategory, setActiveCategory] = useState("All");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [openCategories, setOpenCategories] = useState({});
  const [products, setProducts] = useState([]);
  const [categoryTree, setCategoryTree] = useState({
    "id": 1,
    "name": "All Categories",
    "subcategories": [
      {
        "id": 2,
        "name": "Men",
        "subcategories": [
          {
            "id": 3,
            "name": "Shirts",
            "subcategories": []
          },
          {
            "id": 4,
            "name": "Pants",
            "subcategories": [
              {
                "id": 5,
                "name": "Jeans",
                "subcategories": []
              }
            ]
          }
        ]
      },
      {
        "id": 6,
        "name": "Women",
        "subcategories": [
          {
            "id": 7,
            "name": "Dresses",
            "subcategories": []
          },
          {
            "id": 8,
            "name": "Skirts",
            "subcategories": [
              {
                "id": 9,
                "name": "Mini Skirts",
                "subcategories": []
              },
              {
                "id": 10,
                "name": "Long Skirts",
                "subcategories": []
              }
            ]
          }
        ]
      }
    ]
  }); // Category structure from API
  const [isLoadingCategories, setIsLoadingCategories] = useState(true);

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
  }, []);

  useEffect(() => {
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
  const expandAllCategories = (category) => {
    const openCategories = {};
    const traverse = (node) => {
      openCategories[node.id] = true;
      if (node.subcategories) {
        node.subcategories.forEach(traverse);
      }
    };
    traverse(category);
    setOpenCategories(openCategories);
  };

  const renderCategoryTree = (category, level = 0) => {
    if (!category) return null;

    return (
      <ul key={category.id} className={`level-${level}`}>
        <li>
          <div className="category-item" onClick={() => handleCategoryClick(category.name)}>
            <span>{category.name}</span>
            {category.subcategories && category.subcategories.length > 0 && (
              <span className="toggle" onClick={(e) => { e.stopPropagation(); toggleCategory(category.id); }}>
                {openCategories[category.id] ? "-" : "+"}
              </span>
            )}
          </div>
          {openCategories[category.id] && (
            <ul>
              {category.subcategories.map((subcategory) => renderCategoryTree(subcategory, level + 1))}
            </ul>
          )}
        </li>
      </ul>
    );
  };

  const handleRenderStaticTree = () => {
    setCategoryTree(staticTree);
    expandAllCategories(staticTree);
  };
  return (
    <div className="homepage">
      {/* Sidebar for Categories */}
      <div className="sidebar">
        <h3>Categories</h3>
        <button onClick={handleRenderStaticTree}>Render Static Tree</button>
        <li
          className={`category ${activeCategory === "All" ? "active" : ""}`}
          onClick={() => {
            handleCategoryClick("All");
            expandAllCategories(categoryTree);
          }}
        >
          Show All
        </li>
        {isLoadingCategories ? (
          <p>Loading categories...</p>
        ) : (
          categoryTree && renderCategoryTree(categoryTree)
        )}
      </div>
      {/* Product Grid */}
      <div className="content">
        <div className="product-grid">
          {products.map((product) => (
            <div key={product.productID} className="product-card" onClick={()=>navigate(`/userSession/product/user/${product.productID}`)}>
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

export default HomePage;
