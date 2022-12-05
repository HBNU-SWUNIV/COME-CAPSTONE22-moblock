import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { useRecoilValue } from "recoil";
import { authAtom, transactionAtom } from "../../../../atoms";
import { useQuery } from "react-query";
import { ITransactionResponse } from "../../../../interfaces";
import { fetchTransaction } from "../../../../api";

function TransactionTable() {
  const transactionRequest = useRecoilValue(transactionAtom);
  const jwt = useRecoilValue(authAtom);

  const { isLoading, data } = useQuery<ITransactionResponse>(
    ["allTransaction", transactionRequest],
    async () =>
      await fetchTransaction(jwt.accessToken, transactionRequest).then(
        (response) => response.data
      )
  );

  return isLoading ? (
    <span>loading...</span>
  ) : (
    <TableContainer component={Paper} sx={{
      padding: 2,
      pt: 3,
      borderRadius: 10,
      boxShadow: 3,
      height: "74vh"
    }}>
      <Table aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>시간</TableCell>
            <TableCell align="right">송신자</TableCell>
            <TableCell align="right">수신자</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data!.transferResponseList.map((row, index) => (
            <TableRow
              key={row.transactionId}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                {row.dateCreated.substring(0, 10)}
              </TableCell>
              <TableCell align="right">{row.senderIdentifier}</TableCell>
              <TableCell align="right">{row.receiverIdentifier}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

export default TransactionTable;
