
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../homepage.css";
import "../adminpage.css"; // Styling file
import { toast } from "react-toastify";

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
  const [parentId, setParentId] = useState(NaN); // ID of the parent category
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [catigoriesList, setCategoriesList] = useState([]);

  const notifySuccess = (message)=>{
    toast.success(message);
  }

  const notifyFailier = (message)=>{
    toast.error(message);
  }

  // Handle creating a subcategory
  const handleCreateSubcategory = async () => {

    if (!subcategoryName || !subcategoryDescription || !parentId) {
      setErrorMessage("All fields are required to add a subcategory.");
      return;
    }

    const params = new URLSearchParams({
      categoryName: subcategoryName,
      categoryDescription: subcategoryDescription,
      parentID: parentId,
    });


    try {
      const token = localStorage.getItem('drip_me_up_jwt');
      console.log(params.toString());
      const response = await fetch(`http://localhost:8081/api/7/categories/create?${params.toString()}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
         'Authorization': `Bearer ${token}`
        },
      });

      if (!response.ok) {
        notifyFailier("Failed to create subcategory.");
        throw new Error("Failed to create subcategory.");
      }
      notifySuccess("Subcategory created successfully!");
      setSuccessMessage("Subcategory created successfully!");
      setErrorMessage("");
      setSubcategoryName("");
      setSubcategoryDescription("");
      setParentId(NaN);
      fetchCategories(); // Refresh categories
    } catch (error) {
      notifyFailier("Failed to create subcategory.");
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
         'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
      }
    })
    .then(response=>response.status==200 || response.status==201?(()=>{return response.json()})():(()=>{throw Error("Error fetching all products")})())
    .then(data=>{
      setProducts(data.content);
      setTotalPages(Math.ceil(data.totalItems / ITEMS_PER_PAGE));
    })
  };

  // Function to fetch products for a specific category
  const fetchCategoryProducts = async (category, page = 1) => {
    try {
      const response = await fetch(
        `http://localhost:8081/api/1000/shop/category?category=${category}&page=${page - 1}&size=${ITEMS_PER_PAGE}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            'Authorization': `Bearer ${localStorage.getItem('drip_me_up_jwt')}`
          },
        }
      );
      if (!response.ok){
        console.log(`Error fetching products for category ${category}`);
        setProducts([]);

      } 
      else{
        const data = await response.json();

        // Extract products and handle subcategories
        const products = data.products || [];
        setProducts(products);

        // Calculate total pages based on total items (assuming `data.totalItems` is provided)
        setTotalPages(Math.ceil((data.totalItems || products.length) / ITEMS_PER_PAGE));
      }
    } catch (error) {
      console.error(`Error fetching products for category ${category}:`, error);
    }
  };

  const generateCategoryTree = (categoriesData)=>{
    let tree = {'Male': [], 'Female': []};
    let maleCatigory = categoriesData.filter((selectedCatigory)=>selectedCatigory.name === 'Male')[0];
    let femaleCatigory = categoriesData.filter((selectedCatigory)=>selectedCatigory.name === 'Female')[0];
    setParentId(maleCatigory.id);

    for (let catigory in maleCatigory.subcategoryNames) tree.Male.push(maleCatigory.subcategoryNames[catigory]);
    for (let catigory in femaleCatigory.subcategoryNames) tree.Female.push(maleCatigory.subcategoryNames[catigory]);
    return tree;
  }

  // Function to fetch categories from the API
  const fetchCategories = async () => {
    try {
      setIsLoadingCategories(true);
      const response = await fetch("http://localhost:8081/api/7/categories/");
      if (!response.ok) throw new Error("Failed to fetch categories");

      const data = await response.json();

      setCategoriesList(data);
      setCategoryTree(generateCategoryTree(data));

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
        <div className="addButton" onClick={()=>setShowCategoryFormSwitch(prev=>!prev)} title="Hide add catigory form">-</div>
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
          placeholder="Select parent catigory"
          value={parentId}
          onChange={(e) => setParentId(e.target.value.id)}

        >
          {catigoriesList.map((catigory, key)=>(
            <option key={key} value={catigory}>{catigory.name}</option>
          ))}

          
        </select>

        <button onClick={handleCreateSubcategory}>+ Add Subcategory</button>
      </div>
      </div>
      :<div className="sidebar">
        <h3 style={{display:"flex", alignItems:"center", justifyContent:"space-around"}}>Categories
        <div className="addButton" onClick={()=>setShowCategoryFormSwitch(prev=>!prev)} title="Add catigory form">+</div>
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
            <div key={product.productID} className="product-card" onClick={()=>navigate(`/adminSession/product/admin/${product.productID}/0`)}>
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
