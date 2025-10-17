package data;

import entity.Entity;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {

    // PLAYER STATS
    private int level;
    private int maxLife;
    private int life;
    private int maxMana;
    private int mana;
    private int strength;
    private int dexterity;
    private int exp;
    private int nextLevelExp;
    private int coin;

    // PLAYER INVENTORY
    private ArrayList<String> itemNames = new ArrayList<>();
    private ArrayList<Integer> itemAmounts = new ArrayList<>();
    private int currentWeaponSlot;
    private int currentShieldSlot;
    private int currentBootSlot;

    // OBJECTS ON MAP
    private String[][] mapObjectNames;
    private int[][] mapObjectWorldX;
    private int[][] mapObjectWorldY;
    private String[][] mapObjectLootNames;
    private boolean[][] mapObjectOpened;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(int maxLife) {
        this.maxLife = maxLife;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getNextLevelExp() {
        return nextLevelExp;
    }

    public void setNextLevelExp(int nextLevelExp) {
        this.nextLevelExp = nextLevelExp;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public ArrayList<String> getItemNames() {
        return itemNames;
    }

    public void setItemNames(ArrayList<String> itemNames) {
        this.itemNames = itemNames;
    }

    public ArrayList<Integer> getItemAmounts() {
        return itemAmounts;
    }

    public void setItemAmounts(ArrayList<Integer> itemAmounts) {
        this.itemAmounts = itemAmounts;
    }

    public int getCurrentWeaponSlot() {
        return currentWeaponSlot;
    }

    public void setCurrentWeaponSlot(int currentWeaponSlot) {
        this.currentWeaponSlot = currentWeaponSlot;
    }

    public int getCurrentShieldSlot() {
        return currentShieldSlot;
    }

    public void setCurrentShieldSlot(int currentShieldSlot) {
        this.currentShieldSlot = currentShieldSlot;
    }

    public int getCurrentBootSlot() {
        return currentBootSlot;
    }

    public void setCurrentBootSlot(int currentBootSlot) {
        this.currentBootSlot = currentBootSlot;
    }

    public void setMapObjectNames(String[][] mapObjectNames){
        this.mapObjectNames = mapObjectNames;
    }

    public String[][] getMapObjectNames(){
        return this.mapObjectNames;
    }

    public void setMapObjectNames(int maxMap, int index, String newObjectName){
        this.mapObjectNames[maxMap][index] = newObjectName;
    }

    public String getMapObjectNames(int mapNum, int index){
        return this.mapObjectNames[mapNum][index];
    }

    public void setMapObjectWorldX(int[][] mapObjectWorldX){
        this.mapObjectWorldX = mapObjectWorldX;
    }

    public int[][] getMapObjectWorldX(){
        return this.mapObjectWorldX;
    }

    public void setMapObjectWorldX(int maxMap, int index, int newObjectWorldX){
        this.mapObjectWorldX[maxMap][index] = newObjectWorldX;
    }

    public int getMapObjectWorldX(int maxMap, int index){
        return this.mapObjectWorldX[maxMap][index];
    }

    public void setMapObjectWorldY(int[][] mapObjectWorldY){
        this.mapObjectWorldY = mapObjectWorldY;
    }

    public int[][] getMapObjectWorldY(){
        return this.mapObjectWorldY;
    }

    public void setMapObjectWorldY(int maxMap, int index, int newObjectWorldY){
        this.mapObjectWorldY[maxMap][index] = newObjectWorldY;
    }

    public int getMapObjectWorldY(int maxMap, int index){
        return this.mapObjectWorldY[maxMap][index];
    }

    public void setMapObjectLootNames(String[][] mapObjectLootNames){
        this.mapObjectLootNames = mapObjectLootNames;
    }

    public String[][] getMapObjectLootNames(){
        return this.mapObjectLootNames;
    }

    public void setMapObjectLootNames(int maxMap, int index, String mapObjectLootNames){
        this.mapObjectLootNames[maxMap][index] = mapObjectLootNames;
    }

    public String getMapObjectLootNames(int maxMap, int index){
        return this.mapObjectLootNames[maxMap][index];
    }

    public void setMapObjectOpened(boolean[][] mapObjectOpened){
        this.mapObjectOpened = mapObjectOpened;
    }

    public boolean[][] isMapObjectOpened(){
        return this.mapObjectOpened;
    }

    public void setMapObjectOpened(int maxMap, int index, boolean mapObjectOpened){
        this.mapObjectOpened[maxMap][index] = mapObjectOpened;
    }

    public boolean isMapObjectOpened(int maxMap, int index){
        return this.mapObjectOpened[maxMap][index];
    }
}
