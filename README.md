# MatchingEngine
A FIFO-based order matching engine for trades on a financial exchange.

Current Features: 
1. The engine takes in buy/sell orders for different symbols and matches them with orders on the other side based on best price, earliest order priority.
2. Resting orders can be updated through the unique 'Order ID'.

[TODO]
1. Write test cases for partial fills, order updates, partial fills after order updates.
2. Add feature for symbol classification: https://www.investopedia.com/terms/s/stocksymbol.asp

[IDEAS]
3. Add customer priority for orders based on historical quantity traded/liquidity provided (HFT Bias!).
4. Maker/Taker fees
