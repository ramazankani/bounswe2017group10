import React, { Component } from 'react';
import atlas from './assets/images/atlas.jpeg';
import logo from './assets/images/logo.png';
import { AtlasNavbar } from './Components';
import './css/App.css';

class App extends Component {
  render() {
    return (
      <div className="App">
        <img src={atlas} className="background-image" alt="background" />
        <AtlasNavbar logo={logo} />
        <header className="App-header">
          <h1 className="App-title">Enhance The Culture</h1>
          <h1 className="App-title">Together.</h1>
        </header>
        <footer className="App-footer">
          <a href="#cultural-heritages">See More</a>
        </footer>
      </div>
    );
  }
}

export default App;
