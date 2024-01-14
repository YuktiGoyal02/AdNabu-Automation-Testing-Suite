import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SearchAndAddProducts {
	WebDriver driver;
	@Parameters("browser")
	@BeforeTest
	public void setup(String browser)
	{
		if(browser.equalsIgnoreCase("Mozilla"))
		{
			System.setProperty("webdriver.gecko.driver", "F:/geckodriver.exe");
			driver = new FirefoxDriver();
		}
		else if (browser.equalsIgnoreCase("Chrome"))
		{
			System.setProperty("webdriver.chrome.driver", "F:/chromedriver-win64/chromedriver.exe");
			driver = new ChromeDriver();
		}
		driver.get("https://adnabu-arjun.myshopify.com/");
		driver.manage().window().maximize(); //To maximize the browser window
	}

	@Test(priority=0)
	//Test Case : 1 - Verify the search icon is present on the page and is visible to the user.
	public void isSearchIconVisible() {
		Wait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("header details-modal[class$='header__search']"))));
		Assert.assertTrue(driver.findElement(By.cssSelector("header details-modal[class$='header__search']")).isDisplayed());
	}

	@Test(priority=1)
	//Test case : 2 - Verify on clicking the search icon search box should get opened
	public void isSearchBoxVisible() {
		Wait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("header details-modal[class$='header__search']"))));
		driver.findElement(By.cssSelector("header details-modal[class$='header__search']")).click();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("Search-In-Modal-1"))));
		Assert.assertTrue(driver.findElement(By.id("Search-In-Modal-1")).isDisplayed());
	}

	@Test(priority=2)
	//Test Case : 3 - Verify the search results are relevant to the search text.
	public void isSearchRelevant() {

		String searchText = "bloom";
		WebElement searchArea;
		if(driver.findElement(By.id("Search-In-Modal-1")).isDisplayed()) {
			searchArea = driver.findElement(By.id("Search-In-Modal-1"));		
		}
		else {
			driver.findElement(By.cssSelector("header details-modal[class$='header__search']")).click();
			searchArea = driver.findElement(By.id("Search-In-Modal-1"));
			searchArea.clear();
			searchArea.sendKeys(searchText);
		}
		searchArea.clear();
		searchArea.sendKeys(searchText);
		List<WebElement> searchResults = driver.findElements(By.xpath("(//div[@class='predictive-search__result-group'])[1]/div/ul/li"));
		boolean isRelevant = searchResults.stream().allMatch(result -> result.getText().toLowerCase().contains(searchText.toLowerCase()));
		Assert.assertTrue(isRelevant);
	}

	@Test(priority=3)
	//TestCase 4 - Verify the search result is displayed correctly when no results are found.
	public void noResultFound() {

		String searchText = "xyz";
		WebElement searchArea;
		if(driver.findElement(By.id("Search-In-Modal-1")).isDisplayed()) {
			searchArea = driver.findElement(By.id("Search-In-Modal-1"));

		}
		else {
			driver.findElement(By.cssSelector("header details-modal[class$='header__search']")).click();
			searchArea = driver.findElement(By.id("Search-In-Modal-1"));
		}
		searchArea.clear();
		searchArea.sendKeys(searchText,Keys.ENTER);
		String cartStatus = driver.findElement(By.cssSelector("div[class*=\"template-search__header\"] p")).getText();
		Assert.assertEquals(cartStatus, "No results found for “xyz”. Check the spelling or use a different word or phrase.");
	}

	@Test(priority=4)
	//Test Case : 5 - Verify the search functionality works correctly when multiple search terms are used.
	public void isMultiWordSearchRelevant(){
		Wait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		String multiWord = "wire bloom";
		WebElement searchArea;
		if(driver.findElement(By.id("Search-In-Modal-1")).isDisplayed()) {
			searchArea = driver.findElement(By.id("Search-In-Modal-1"));

		}
		else {
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("header details-modal[class$='header__search']"))));
			driver.findElement(By.cssSelector("header details-modal[class$='header__search']")).click();
			searchArea = driver.findElement(By.id("Search-In-Modal-1"));

		}
		searchArea.clear();
		searchArea.sendKeys(multiWord);
		List<WebElement> multiSearchResults = driver.findElements(By.xpath("(//div[@class='predictive-search__result-group'])[1]/div/ul/li"));
		boolean isMultiSearchRelevant = multiSearchResults.stream().allMatch(result -> result.getText().toLowerCase().contains(multiWord.toLowerCase()));
		Assert.assertTrue(isMultiSearchRelevant);

	}

	@Test(priority=5)
	//Test Case : 6 - Verify the search functionality by searching for a term with a mix of upper and lower case letters.
	public void isMixSearchRelevant(){
		Wait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		String mixCaseSearch = "BLOom";
		WebElement searchArea;
		if(driver.findElement(By.id("Search-In-Modal-1")).isDisplayed()) {
			searchArea = driver.findElement(By.id("Search-In-Modal-1"));

		}
		else {
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("header details-modal[class$='header__search']"))));
			driver.findElement(By.cssSelector("header details-modal[class$='header__search']")).click();
			searchArea = driver.findElement(By.id("Search-In-Modal-1"));
		}
		searchArea.clear();
		searchArea.sendKeys(mixCaseSearch);
		List<WebElement> mixSearchResults = driver.findElements(By.xpath("(//div[@class='predictive-search__result-group'])[1]/div/ul/li"));
		boolean isMixSearchRelevant = mixSearchResults.stream().allMatch(result -> result.getText().toLowerCase().contains(mixCaseSearch.toLowerCase()));
		Assert.assertTrue(isMixSearchRelevant);

	}

	@Test(priority=6)
	//Test Case : 7 - Click any product from the search list, the correct product should be displayed on the page
	public void isProductValid() {
		Wait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement searchArea;
		if(driver.findElement(By.id("Search-In-Modal-1")).isDisplayed()) {
			searchArea = driver.findElement(By.id("Search-In-Modal-1"));

		}
		else {
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("header details-modal[class$='header__search']"))));
			driver.findElement(By.cssSelector("header details-modal[class$='header__search']")).click();
			searchArea = driver.findElement(By.id("Search-In-Modal-1"));

		}
		searchArea.clear();
		searchArea.sendKeys("bloom",Keys.ENTER);
		List<WebElement> productList = driver.findElements(By.cssSelector("#product-grid ul li"));
		for(WebElement product : productList) {
			if(product.getText().toLowerCase().contains("18k Bloom Pendant".toLowerCase())) {
				product.click();
				break;
			}
		}
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div[class=\"product__title\"]"))));
		Assert.assertEquals(driver.findElement(By.cssSelector("div[class=\"product__title\"]")).getText().toLowerCase(), "18k Bloom Pendant".toLowerCase());

	}

	@Test(priority=7)
	//Test Case : 8 - click on add to cart button, verify the product is not sold out and can be added in the cart
	public void addToCart(){
		Wait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("ProductSubmitButton-template--14768207495265__main"))));
		String productStatus = driver.findElement(By.id("ProductSubmitButton-template--14768207495265__main")).getText();
		if(productStatus.toLowerCase().equalsIgnoreCase("add to cart")) {
			driver.findElement(By.id("ProductSubmitButton-template--14768207495265__main")).click();
		}

	}

	@Test(priority=8)
	//Test Case : 9 - Go to cart, the added product should be present in the cart
	public void isProductAdded(){
		Wait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("cart-notification-button"))));
		driver.findElement(By.id("cart-notification-button")).click();
		wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#main-cart-items div table tbody tr"))));
		List<WebElement> cartItems = driver.findElements(By.cssSelector("#main-cart-items div table tbody tr"));
		for(WebElement item : cartItems) {
			if(item.getText().toLowerCase().contains("18k Bloom Pendant".toLowerCase())) {
				Assert.assertTrue(item.getText().toLowerCase().contains("18k Bloom Pendant".toLowerCase()));
				break;
			}
		}
	}

	@Test(priority=9)
	//Test Case : 10 - Click delete icon to remove the product from the list
	public void removeFromCart(){
		Wait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#main-cart-items div table tbody tr"))));
		List<WebElement> cartItems = driver.findElements(By.cssSelector("#main-cart-items div table tbody tr"));
		for(WebElement item : cartItems) {
			if(item.getText().toLowerCase().contains("18k Bloom Pendant".toLowerCase())) {
				driver.findElement(By.cssSelector("#main-cart-items div table tbody tr td[class=\"cart-item__quantity\"] quantity-popover div[class*=\"cart-item__quantity-wrapper\"] cart-remove-button")).click();
				break;
			}
		}
	}

	@Test(priority=10)
	//Test Case : 11 - verify the item is removed from the cart
	public void isRemoved(){
		Wait<WebDriver> wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#main-cart-items div table tbody tr"))));
		List<WebElement> cartItems = driver.findElements(By.cssSelector("#main-cart-items div table tbody tr"));
		if(!driver.findElement(By.cssSelector("div[class=\"cart__warnings\"] h1")).getText().equalsIgnoreCase("Your cart is empty")) {
			for(WebElement item : cartItems) {
				try {
					item.getText().toLowerCase().equalsIgnoreCase("18k Bloom Pendant".toLowerCase());
				}
				catch (org.openqa.selenium.StaleElementReferenceException e) {
					System.out.println("Success: ProductXYZ is removed from the cart (StaleElementReferenceException).");
				}
			}
		}
	}

	@AfterTest
	public void quit() {
		if (driver != null) {
			driver.quit();
		}
	}
}
