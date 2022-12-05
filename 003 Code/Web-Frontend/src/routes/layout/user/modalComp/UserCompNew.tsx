import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import { InputLabel, NativeSelect } from "@mui/material";
import { UserRole } from "../../../../interfaces";
import * as React from "react";
import { useState } from "react";
import { fetchCreateUser } from "../../../../api";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { authAtom, modalStateAtom } from "../../../../atoms";
import LoadingButton from "@mui/lab/LoadingButton";

const style = {
  position: "absolute" as "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  p: 4
};

function UserCompNew() {
  const [loading, setLoading] = useState(false);
  const [userId, setUserId] = useState("");
  const [userPassword, setUserPassword] = useState("");
  const [userName, setUserName] = useState("");
  const [userRole, setUserRole] = useState(UserRole.ROLE_STUDENT);
  const jwt = useRecoilValue(authAtom);
  const setModalState = useSetRecoilState(modalStateAtom);

  return (
    <Box sx={style}>
      <Typography id="modal-modal-title" variant="h6" component="h2">
        계정 생성
      </Typography>
      <Grid container spacing={1} sx={{ mt: 2 }}>
        <Grid item xs={12}>
          {/*<InputLabel variant="standard" htmlFor="coin-native">*/}
          {/*  ID*/}
          {/*</InputLabel>*/}
          <TextField
            fullWidth
            size="small"
            sx={{ mt: 1 }}
            label="ID"
            variant="outlined"
            onChange={(event) => setUserId(event.target.value)}
          />
        </Grid>
        <Grid item xs={12}>
          {/*<InputLabel variant="standard" htmlFor="coin-native">*/}
          {/*  Password*/}
          {/*</InputLabel>*/}
          <TextField
            fullWidth
            size="small"
            sx={{ mt: 1 }}
            label="Password"
            variant="outlined"
            onChange={(event) => setUserPassword(event.target.value)}
          />
        </Grid>
        <Grid item xs={9}>
          {/*<InputLabel variant="standard" htmlFor="coin-native">*/}
          {/*  Name*/}
          {/*</InputLabel>*/}
          <TextField
            fullWidth
            size="small"
            sx={{ mt: 1 }}
            label="Name"
            variant="outlined"
            onChange={(event) => setUserName(event.target.value)}
          />
        </Grid>
        <Grid item xs={3}>
          <InputLabel variant="standard" htmlFor="coin-native">
            Role
          </InputLabel>
          <NativeSelect
            onChange={(event) =>
              setUserRole(
                event.target.value == "1"
                  ? UserRole.ROLE_STUDENT
                  : UserRole.ROLE_STOREMANAGER
              )
            }
            inputProps={{
              name: "Role",
              id: "role-native"
            }}
          >
            <option value={1}>학생</option>
            <option value={2}>상점</option>
          </NativeSelect>
        </Grid>
      </Grid>
      <br />
      <Box display="flex">
        <LoadingButton
          loading={loading}
          sx={{ ml: "auto", mt: 1 }}
          variant="contained"
          onClick={() => {
            setLoading((prevState) => !prevState);
            fetchCreateUser(jwt.accessToken, {
              name: userName,
              userRole: userRole,
              identifier: userId,
              password: userPassword
            })
              .then((response) => {
                setLoading((prevState) => !prevState);
                setModalState((prevState) => !prevState);
                alert("생성 성공");
              })
              .catch((e) => {
                alert(e.response.data.message);
                setModalState((prevState) => !prevState);
              });
          }}
        >
          유저 생성
        </LoadingButton>
      </Box>
    </Box>
  );
}

export default UserCompNew;
