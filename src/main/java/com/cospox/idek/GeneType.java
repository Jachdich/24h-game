package com.cospox.idek;


public enum GeneType {
	NONE('N'),
	REPAIR_MEMBRANE('M'),
	DIVIDE('U'),
	PRODUCE_ATP('A'),
	GENE_TO_RNA('R'),
	RNA_TO_GENE('I'),
	DIVIDE_UNTIL('D'),
	MAKE_VIRUS_SHELL('V'),
	REMOVE_WASTE('W');
	private final char letter;
	
	GeneType(char string) {
		letter = string;
	}

	public String toString() {
		return String.valueOf(letter);
	}
	
	public String humanName() {
		switch (this) {
		case GENE_TO_RNA: 	          return "Gene to RNA";
		case DIVIDE:                  return "Divide";
		case DIVIDE_UNTIL:            return "Divide (unless >30 cell)";
		case RNA_TO_GENE:             return "RNA to gene";
		case NONE:					  return "None";
		case PRODUCE_ATP:			  return "Produce ATP";
		case REPAIR_MEMBRANE:         return "Repair membrane";
		case REMOVE_WASTE:            return "Remove waste";
		case MAKE_VIRUS_SHELL:        return "Make virus shell";
		default:                      return "Unknown";
		}
	}
	
	public char getLetter() { return letter; }
	
	public int[] getColour() {
		switch (this) {
		case GENE_TO_RNA: 	          return new int[]{0xFFFFFFFF, 0xFFFF22FF};
		case DIVIDE:                  return new int[]{0xFFFFFFFF, 0xFFFF9090};
		case DIVIDE_UNTIL:            return new int[]{0xFFFFFFFF, 0xFFDD2211};
		case RNA_TO_GENE:             return new int[]{0xFF000000, 0xFF22FF44};
		case NONE:					  return new int[]{0xFFFFFFFF, 0xFF111111};
		case PRODUCE_ATP:			  return new int[]{0xFF000000, 0xFFFFFF00};
		case REPAIR_MEMBRANE:         return new int[]{0xFFFFFFFF, 0xFF2222FF};
		case REMOVE_WASTE:            return new int[]{0xFFFFFFFF, 0xFF804000};
		case MAKE_VIRUS_SHELL:        return new int[]{0xFFFFFFFF, 0xFFFFA000};
		default:                      return new int[]{0xFFFFFFFF, 0xFF000000};
		}
	}

}
