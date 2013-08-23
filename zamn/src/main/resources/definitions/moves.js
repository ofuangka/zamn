{
	"throw_rock" : {
		"name" : "Throw rock",
		"targetingRange" : {
			"type" : "DECAYING",
			"range" : 3,
			"filter" : "NULL_TILE_FILTER",
			"inclusive" : false
		},
		"actualRangeFilter" : "OCCUPIED_BY_CRITTER_FILTER",
		"areaOfEffect" : {
			"type" : "DECAYING",
			"range" : 0,
			"filter" : "NULL_TILE_FILTER",
			"inclusive" : true
		},
		"effect" : {
			"min" : 1,
			"max" : 2,
			"drivingStat" : "STRENGTH",
			"affectedStat" : "HP",
			"positive" : false
		},
		"mpCost" : 5
	},
	"fire_bomb" : {
		"name" : "Fire bomb",
		"targetingRange" : {
			"type" : "DECAYING",
			"range" : 2,
			"filter" : "NULL_TILE_FILTER",
			"inclusive" : false
		},
		"actualRangeFilter" : "OCCUPIED_BY_CRITTER_FILTER",
		"areaOfEffect" : {
			"type" : "DECAYING",
			"range" : 1,
			"filter" : "NULL_TILE_FILTER",
			"inclusive" : true
		},
		"effect" : {
			"min" : 1,
			"max" : 2,
			"drivingStat" : "SMARTS",
			"affectedStat" : "HP",
			"positive" : false
		},
		"mpCost" : 5
	},
	"default_attack" : {
		"name" : "Attack",
		"targetingRange" : {
			"type" : "DECAYING",
			"range" : 1,
			"filter" : "NULL_TILE_FILTER",
			"inclusive" : false
		},
		"actualRangeFilter" : "OCCUPIED_BY_CRITTER_FILTER",
		"areaOfEffect" : {
			"type" : "DECAYING",
			"range" : 0,
			"filter" : "NULL_TILE_FILTER",
			"inclusive" : true
		},
		"effect" : {
			"min" : 1,
			"max" : 2,
			"drivingStat" : "STRENGTH",
			"affectedStat" : "HP",
			"positive" : false
		},
		"mpCost" : 0
	}
}