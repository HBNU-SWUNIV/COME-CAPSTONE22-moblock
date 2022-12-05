import * as React from "react";
import { useQuery } from "react-query";
import { IShopDetail } from "../../../../interfaces";
import { fetchAllStore } from "../../../../api";
import { useRecoilValue } from "recoil";
import { authAtom } from "../../../../atoms";
import TableContainer from "@mui/material/TableContainer";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";

function ShopList() {
  const jwt = useRecoilValue(authAtom);
  const { isLoading, data } = useQuery<IShopDetail>(
    ["allShop"],
    async () =>
      await fetchAllStore(jwt.accessToken, 1).then(
        (response) => response.data
      )
  );

  console.log(data);


  return isLoading ? <span>loading...</span> :
    (<TableContainer component={Paper} sx={{
        padding: 2,
        pt: 3,
        borderRadius: 10,
        boxShadow: 3,
        height: "37vh"
      }}>
        <Table aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell>상점 명</TableCell>
              <TableCell align="right">전화번호</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data!.storeResponseList.map((row, index) => (
              index < 4 ?
                <TableRow
                  key={row.phoneNumber}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <TableCell component="th" scope="row">
                    {row.name}
                  </TableCell>
                  <TableCell align="right">{row.phoneNumber}</TableCell>
                </TableRow>
                : <span />

            ))}
          </TableBody>
        </Table>
      </TableContainer>
    );
}

export default ShopList;