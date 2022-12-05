import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";

function LoginHeader() {
  return (
    <Box sx={{ flexGrow: 1, minWidth: "100vw" }}>
      <AppBar position="static" sx={{
        backgroundColor: "#90CAF9"
      }}>
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Hanbat Currency Manager
          </Typography>
        </Toolbar>
      </AppBar>
    </Box>
  );
}

export default LoginHeader;
