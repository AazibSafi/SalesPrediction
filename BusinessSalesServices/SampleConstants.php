<?php
class SampleConstants {

	private $CompaniesList = 
			array(
					array(
						"name" => "Dove",
			            "products" => array (
				            	"Dove Intensive Repair Shampoo (500ml)",
				                "Dove Hair Fall Solution Shampoo (500ml)",
				                // "Dove Dryness Care Shampoo(500ml)",
				                "Dove Daily Shine Shampoo (500ml)"
				            )
		            	),
					array(
						"name" => "Sunsilk",
			            "products" => array (
			            		"Sunsilk Black Shine Shampoo(500ml)",
				                "Sunsilk Pink (Lusciously thick and Long shampoo) 500ml",
				                "Sunsilk Hair Fall Solution Shampoo (500ml)"
				                // "Sunsilk perfect Straight Shampoo (500ml) purple",
				                // "Sunsilk Dream soft and Smooth Shampoo(500ml) Yellow"
				            )
		            	),
					array(
						"name" => "Pantene",
			            "products" => array (
				                "Pantene Damage Detox Shampoo (500ml)",
				                "Pantene Smooth And Sleek Shampoo (500ml)",
				                "Pantene Color Preserve Shine Shampoo (500ml)"
				            )
		            	),
					array(
						"name" => "Head and Shoulder",
			            "products" => array (
				               "Head and Shoulder Daily Scalp Care Shampoo(500ml)",
				                "Head and Shoulder Classic Clean Shampoo(500ml)",
				                // "Head and Shoulder Clinical Health Shampoo(Blue) (500ml)",
				                // "Head and Shoulder Smooth and Silky Shampoo(500ml)",
				                "Head and Shoulder Anti Dandruff Shampoo(500ml)"
				                // "Head and Shoulder Repair and Protect Shampoo(500ml)"
				            )
		            	)
				);

	private $RegionsList = array("Malir", "Gulshan", "Johar", "Defence", "Korangi");

	private $YearDetails = array('min' => 2014, 'max' => 2017, 'months' => 12 );

	private $minAmount = 0;
	private $maxAmount = 10000;

	public function getCompanies() {
		return $this->CompaniesList;
	}

	public function getRegionsList() {
		return $this->RegionsList;
	}

	public function getYearDetails() {
		return $this->YearDetails;
	}

	public function getMinAmount() {
		return $this->minAmount;
	}

	public function getMaxAmount() {
		return $this->maxAmount;
	}
}
?>