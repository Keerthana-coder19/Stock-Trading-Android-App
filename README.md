# Stock-Trading-Android-App

#Reference
-----------
Android Stock Trading App (Android OS, JAVA,Volley,Glide,SectionedRecyclerAdapter,Grid View,Search widget,Webview,Progress Bar)
---------------------------
o	Implemented features for stock trading app with splash screen, home screen with portfolio displaying net worth and favorites supporting swipe to delete features with GCP hosted node.js backend JSON data fetched via Tiingo API using volley HTTP library
o	Enhanced to support autocomplete stock symbol search, detailed stock info screen with the associated latest stock news integrated with twitter content sharing and stock buy and sell function with real time net worth and latest price reporting user-friendly functionalities.
o	Incorporates usage of shared preferences to store and communicate data between home and details screen
o	Developed and tested using SDK API 29 in native emulator

Description
------------
This is an Android application, which allows users to search for different
stock symbols/tickers and look at the detailed information about them. Additionally, the users can
trade with virtual money and create a portfolio. Users can also favorite stock symbols to track their
stock prices. The App contains 2 screens: Home screen and the Detailed Stock Information
screen. However, the App has multiple features on each of these screens.

Implementation Details
-----------------------

 App Icon and Splash Screen
 ----------------------------
 
 Home screen
 --------------
 When you open the app, there will be an initial spinner while the data is being fetched using volley. The home screen will have a toolbar at the top with Stocks title and the
search icon.

There are 2 sections on the home screen:
● Portfolio Section - This section will show the total net worth of the user, which is
calculated as the sum of number of shares of a stock multiplied by the current price, plus
uninvested cash. This is followed by the list of stocks in the user portfolio with their
current price, change in price and total shares owned information.
● Favorites Section - This section will show all the stocks that have been favorited by the
user to allow the user to easily check the prices of stocks in their watchlist. The stock
symbol, current price, change in price and company name should be displayed. 
In case the favorited stock is present in the user portfolio, instead of the
company name, the stocks owned should be displayed.
Additionally, the symbol next to the change in price value should either be trending down or up
based on the change price value. For example, if the change in price is positive, a trending up
symbol should be displayed and the symbol as well as the change price value should have green
color. In case the change in price is zero i.e. no change, no symbol should be displayed, and the
change price value should be grey.
Each stock listing also has a button on the extreme right, next to the current price field. On clicking
the button or the stock listing, the detailed information screen will open for the selected stock.

The home screen view should support multiple functionalities like:
● The swipe to delete functionality allows the user to remove/delete the stock from the
favorite section. On removing a stock from the favorite section, the stock should be
removed from the favorite stocks in the local storage and the view.

At the bottom of the 2 sections, we have a ‘Powered by tiingo’ text in italic. On clicking this text,
the App should open the Tiingo homepage in chrome.

The home screen has been implemented by using a RecyclerView with the
SectionedRecyclerViewAdapter. Each of the stock listings has been implemented using
ConstraintLayout, TextView, ImageView.

The Search button on the toolbar opens the search bar to type the stock symbol to search. The
search bar uses the autocomplete functionality.

Search Functionality
--------------------
On top right side, there will be a search button which opens a textbox where the user can
enter a keyword to search for a stock symbol. See hints for icon.
● The user is provided with suggestions of keywords using the Tiingo Autocomplete API.
● When the user taps on a suggestion, it is filled inside the search box and clicking enter/next
takes the user to the detailed information screen.
● Before you get the data from your backend server, a progress bar should display on the
screen as indicated in the detailed information section.
● The user can only search for valid stock symbols in the search bar. The search should
redirect to the detailed information screen only if the user selected one of the autocomplete
suggestions to fill the search field.
● In the Autosuggest, only make an API call after the user enters 3 characters. Example:
“am” should not display any suggestions but “ama” should have suggestions.

This component involves 2 things:
● Implementing a searchable
● Implementing autocomplete

Detailed Stock Information Screen
----------------------------------

On clicking the Goto button on any stock listing or searching for a stock symbol, the loading
spinner symbol should be displayed while the details are being fetched. Once the
data has been fetched except the chart (since the chart takes longer to load), the spinner should
disappear and information regarding the stock should be available to the user.

The top action bar should have the ‘Stock’ title and the back button to go back to the home screen
(which has the filter values that were used for the current search if triggered by using the search
functionality). The action bar should also contain a favorite icon to add or remove the stock from
favorites. The favorite icon will either be filled or bordered based on whether the stock is favorited
or not. Adding/Removing the stock from favorites should also display a toast message.

Below the action bar, there should be 4 fields: stocks symbol, current price with ‘$’ sign, company
name and the change price with ‘$’ sign (the text color should be green, red or grey based on the
change price value being positive, negative or zero respectively). The App then has a WebView
element which is blank till the chart loads.

The Portfolio section allows the user to trade the shares of the stock. It contains a left section
which shows the market value of the stock in the user portfolio and the number of shares the user
owns. The right section contains the trade button. Initially, when the user starts the app for the
first time, they will not have any stocks/shares in the portfolio and an initial pre-loaded amount of
$20,000 to trade on the app. This amount can change based on the trading done by the user.
(For example, if the user sells shares at a loss, it can become less than 20,000 and so forth)
If the user does not own any shares of the given stock, the left section will have the appropriate messages.

The Stats section displays the trading statistics for the given stock in a grid. The grid has 7
fields namely: Current price, Low, Bid price, Open price, Mid, High and Volume. If any of these
fields are missing in the JSON, set them as 0.0. The GridView element is to be used for this
section.
The About section displays the description of the company. If the description is longer than 2
lines, ellipsize the end of the 2nd line and display a ‘Show more…’ button. On clicking this
button, the complete description becomes visible and the button text changes to ‘Show less’.
If the description is less than 2 lines, do not display the button.


The News section displays the news articles related to the given stock symbol. The first article
has a different format/layout than the rest of the articles in the list. On clicking the news article,
the original article is opened in chrome using the article URL. On long press, a dialog box opens
with options to share on twitter and open in chrome.For each article, the information
displayed is Article source, Article title, Article image and the time ago when the article was
published. The time ago should support ‘days ago’ and ‘minutes ago’ by calculating the difference
between the timestamp the article was published and the current timestamp.
The news section uses RecyclerView and ArticleDialog elements.

From every twitter button, on clicking the button, the article should be shared by opening a
browser with Twitter Intent.

The Trade button in the Portfolio section opens a new dialog box for trading. The
dialog shows an input box which only accepts numeric input. Below the input field, there is a
calculation text box which updates based on the numeric input to display the final price of the
trade. The trade dialog also displays the current available amount to trade for the user. The user
can either buy or sell the shares. Based on the trade, the amount available to trade will be updated
accordingly.

HighCharts in Android
---------------------
The Chart section in the detailed stock information screen uses a WebView element to load the
HighCharts stock chart.To load the chart, the App will load
a local HTML file with the necessary JavaScript to request the data from the NodeJS server and
display the chart when the data is fetched.

Progress bar
-------------
Every time the user has to wait before they can see the data, you must display a progress bar. 
The progress bar is to be present across the Home screen, Detailed Stock screen and just says “Fetching Data…”.
The progress bar is displayed by default (where needed) and then hidden as soon as the data is received/view is prepared.

All HTTP/HTTPS requests are made to the backend Node.js on GAE.
● All HTTP requests are asynchronous and do not block the main UI thread. 

