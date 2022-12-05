import Grid from "@mui/material/Grid";
import CoinChart from "./component/CoinChart";
import * as React from "react";
import { useState } from "react";
import Box from "@mui/material/Box";
import TransactionTable from "./component/TransactionTable";
import { useQuery } from "react-query";
import { ICoinDetail } from "../../../interfaces";
import { fetchAllCoins } from "../../../api";
import { useRecoilValue } from "recoil";
import { authAtom } from "../../../atoms";
import { NativeSelect } from "@mui/material";
import CoinShareChart from "./component/CoinShareChart";
import ShopList from "./component/ShopList";


function HomeComp() {
  const jwt = useRecoilValue(authAtom);
  const [coinOne, setCoinOne] = useState(0);
  const [coinTwo, setCoinTwo] = useState(1);
  const [totalIssuance, setTotalIssuance] = useState(0);

  const { isLoading, data } = useQuery<ICoinDetail>(["allCoins"], async () =>
    await fetchAllCoins(jwt.accessToken)
      .then((response) => response.data)
      .then(async (res) => {
        await setTotalIssuance(res.totalIssuance);
        return res;
      })
  );

  return isLoading ? (
    <span>loading...</span>
  ) : (
    <Grid container spacing={1}>
      <Grid item xs={8}>
        <Grid container spacing={1}>
          <Grid item xs={6}>
            <ShopList />
          </Grid>
          <Grid item xs={6}>
            <Box
              sx={{
                padding: 2,
                pt: 3,
                borderRadius: 10,
                boxShadow: 3,
                height: "37vh"
              }}
            >
              <CoinShareChart coinList={data!.coinDtoList} totalIssuance={totalIssuance} />
            </Box>
          </Grid>

          <Grid item xs={6}>
            <Box
              sx={{
                padding: 2,
                pt: 3,
                borderRadius: 10,
                boxShadow: 3,
                height: "37vh"
              }}
            >
              <Box display="flex" sx={{ mb: 1 }}>
                <NativeSelect
                  sx={{ ml: "auto" }}
                  onChange={(event) => setCoinOne(+event.target.value)}
                  inputProps={{
                    name: "Coin",
                    id: "coin-native"
                  }}
                >
                  {data!.coinDtoList.map((currElement, index) => <option value={index}>{currElement.name}</option>)}
                </NativeSelect>
              </Box>
              <CoinChart coinName={data!.coinDtoList[coinOne]?.name} />
            </Box>
          </Grid>
          <Grid item xs={6}>
            <Box
              sx={{
                padding: 2,
                pt: 3,
                borderRadius: 10,
                boxShadow: 3,
                height: "37vh"
              }}
            >
              <Box display="flex" sx={{ mb: 1 }}>
                <NativeSelect
                  sx={{ ml: "auto" }}
                  onChange={(event) => setCoinTwo(+event.target.value)}
                  inputProps={{
                    name: "Coin",
                    id: "coin-native"
                  }}
                >
                  {data!.coinDtoList.map((currElement, index) => <option value={index}>{currElement.name}</option>)}
                </NativeSelect>
              </Box>
              <CoinChart coinName={data!.coinDtoList[coinTwo]?.name} />
            </Box>
          </Grid>
          <Grid item xs={6}>
          </Grid>
        </Grid>
      </Grid>
      <Grid item xs={4}>
        <TransactionTable />
      </Grid>
    </Grid>
  );
}

export default HomeComp;
