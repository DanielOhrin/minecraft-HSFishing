# HSFishing Documentation
  
  HSFishing adds a custom leveling and fishing system. Instead of receiving normal drops or using a normal fishing rod, you must define custom fishing rods in the config, and add to their loot-table in-game.  

## Level System

The leveling system is contained within the `CustomLevelSystem` class.  
The maximum level is easily changeable, and the xp required for each level utilizes the following equation: (level^2)*100  
  
By default, the maximum level is 100, but this is easily changeable in the source code.  

## Config/Fishing Rods

`config.yml` is currently only used to define new Fishing rod milestones.  
Every key is a "milestone," which just means it will require the player to use /upgraderod to upgrade from the previous rod.  
  
  Format:  
  The first rod must have minimum-level of 1.  
  Each rod must have the following keys:  
  - minimum-level -> Minimum level the rod will upgrade into this.
  - display-name -> Color coded name of the rod. Example: "&b&lOld Rod"
  - lore -> First section of lore, before the experience and perks.
  - particle -> Trail of items fished up.
  - drop-table -> No value is fine

## Drops System

The drops system is fairly simple. When a player adds a `Drop`, their held itemstack is serialized using the `ItemSerializer` class, and added to the config. This process is in the `HSFishingCommand` class under /hsfishing rod addDrop. 
  
Drop chances are based off of weight. No matter what order the drops are in config, they will be ordered in descending order by weight before a drop is chosen. A drop is chosen by generating a random number up to the total weight and subtracting each item's weight until the random number is 0 or less.

## Perks

Perks are defined in the `Perks` class. A perk is applicable to an `HSFishingRod`, or to the `FishCaughtEvent`. For example, there might be a DoubleExperience perk that is not applicable to the rod, but to the event itself.  
- ExperienceMultiplier - multiplies experience gained
- ItemLuck - adds weight to the random weight number when it would not exceed the total weight.

## Upgrading a rod

To upgrade an HSFishingRod, it must meet the level requirement, then levels up automatically. Experience does not overflow into the next level. Once it reaches enough experience to unlock the next milestone, the player can type `/upgraderod` to upgrade it.

## Events

`FishCaughtEvent` (Cancellable) - called when a player catches a fish with an `HSFishingRod`. Can add/remove drops and perks from this.  
`RodLevelUpEvent` - called when an `HSFishingRod` levels up. Does not call on milestone upgrade.  
`RodMilestoneUnlockedEvent` - called when an `HSFishingRod` is ready to use /upgraderod.  
`RodUpgradedEvent` - called when an `HSFishingRod` 

## Rarity

Each drop is given a rarity based on the percent chance it has to be obtained. This rarity can be also be set manually.

Rarities:
- Common: 40%+
- Uncommon: 15%+
- Rare: 5%+
- Epic: 1%+
- Legendary: Anything below 1%
- Unique
- Special

Unique and Special are currently unused by the plugin itself. These rarities and their colors follow the universal rarity system of HighSkiesMC.  
The % chances can be edited in the `DropTable` class.