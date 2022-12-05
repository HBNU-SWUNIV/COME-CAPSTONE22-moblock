import * as React from "react";
import { useState } from "react";
import Box from "@mui/material/Box";
import Drawer from "@mui/material/Drawer";
import CssBaseline from "@mui/material/CssBaseline";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import List from "@mui/material/List";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import HomeComp from "./dashboard/HomeComp";
import UserComp from "./user/UserComp";
import TransactionComp from "./transaction/TransactionComp";
import CoinComp from "./coin/CoinComp";
import ShopComp from "./shop/ShopComp";
import HomeIcon from "@mui/icons-material/Home";
import CurrencyBitcoinIcon from "@mui/icons-material/CurrencyBitcoin";
import GroupIcon from "@mui/icons-material/Group";
import StoreIcon from "@mui/icons-material/Store";
import ReceiptLongIcon from "@mui/icons-material/ReceiptLong";

const drawerWidth = 240;

function Dashboard() {
  const [mainComponent, setMainComponent] = useState(<HomeComp />);
  const sidebar = [
    <HomeComp />,
    <UserComp />,
    <CoinComp />,
    <ShopComp />,
    <TransactionComp />
  ];
  const sidebarIcon = [
    <HomeIcon />,
    <GroupIcon />,
    <CurrencyBitcoinIcon />,
    <StoreIcon />,
    <ReceiptLongIcon />
  ];

  const handleSideClick = (
    event: React.MouseEvent<HTMLDivElement>,
    index: number
  ) => {
    setMainComponent(sidebar[index]);
  };

  return (
    <Box sx={{ display: "flex" }}>
      <CssBaseline />
      {/*해더 파트*/}
      <AppBar
        position="fixed"
        style={{
          background: "#90CAF9"
        }}
        sx={{
          width: `calc(100% - ${drawerWidth}px)`,
          ml: `${drawerWidth}px`
        }}
      >
        <Toolbar>
          <Typography variant="h6" noWrap component="div">
            Hanbat Currency Manager
          </Typography>
        </Toolbar>
      </AppBar>
      {/*서랍 파트*/}
      <Drawer
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          "& .MuiDrawer-paper": {
            width: drawerWidth,
            boxSizing: "border-box"
          }
        }}
        variant="permanent"
        anchor="left"
      >
        <Toolbar>
            <img alt="hanbatLogo" src="/hanbatLogo.jpg" width='100%' height='80%'/>
        </Toolbar>
        <Divider />
        <List>
          {["홈", "사용자 관리", "코인 관리", "가맹점 관리", "트랜잭션"].map(
            (text, index) => (
              <ListItem key={text} disablePadding>
                <ListItemButton
                  onClick={(event) => {
                    handleSideClick(event, index);
                  }}
                >
                  <ListItemIcon>{sidebarIcon[index]}</ListItemIcon>
                  <ListItemText primary={text} />
                </ListItemButton>
              </ListItem>
            )
          )}
        </List>
        <Divider />
      </Drawer>
      {/*본문 파트*/}
      <Box
        component="main"
        sx={{ flexGrow: 1, bgcolor: "background.default", p: 3 }}
      >
        <Toolbar />
        {mainComponent}
      </Box>
    </Box>
  );
}

export default Dashboard;
