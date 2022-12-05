import React from "react";
import Router from "./Router";
import { Helmet } from "react-helmet";

function App() {
  return (
    <>
      <Helmet>
        <title>한밭 코인 관리자</title>
      </Helmet>
      <Router />
    </>
  );
}

export default App;
