import React from 'react'
import '../style/Navbar.css'


function Navbar(){
    return(
        <nav>
                <ul className="liste">
                    <li  className="items">  Mon agenda   </li>
                    <li className="items" > Statistiques </li>
                    <li className="items" > Mentions Légales </li>
                </ul>
        </nav>
    )
}

export default Navbar ;