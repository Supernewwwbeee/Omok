/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OMG;

/**
 *
 * @author twins
 */
public class CheckResult {

    public static ThreeCheckResult checkThree(int[][] data, int chX, int chY, int turn) {

        ThreeCheckResult result = new ThreeCheckResult();

        int maxIndex = data[0].length - 1;

        if (chX != 0 || chX != maxIndex || chY != 0 || chY != maxIndex) {

            int stone1 = 0;
            int stone2 = 0;
            int stonesum = 0;
            int blank1 = 0;
            int blank2 = 0;

            // ← 탐색
            int tempY = chY - 1;
            while (tempY >= 0 && data[chX][tempY] == turn) {
                stone1++;
                tempY--;
            }
            if (tempY >= 0 && data[chX][tempY] == 0) {
                blank1++;
                tempY--;
            }
            while (tempY >= 0 && data[chX][tempY] == turn) {
                stone1++;
                tempY--;
            }
            if (tempY >= 0 && data[chX][tempY] == 0) {
                blank1++;
                tempY--;
            }

            // → 탐색
            tempY = chY + 1;
            while (tempY <= maxIndex && data[chX][tempY] == turn) {
                stone2++;
                tempY++;
            }
            if (tempY <= maxIndex && data[chX][tempY] == 0) {
                blank2++;
                tempY++;
            }
            while (tempY <= maxIndex && data[chX][tempY] == turn) {
                stone2++;
                tempY++;
            }
            if (tempY <= maxIndex && data[chX][tempY] == 0) {
                blank2++;
                tempY++;
            }

            // 열린 3인지 판단
            stonesum = stone1 + stone2;

            if (stonesum == 2) {

                if (stone1 == 2) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.leftright1++;
                        if ((blank1 == 1) && (blank2 == 2)) {
                            if (data[chX][chY - (stone1 + blank1)] == turn) {
                                result.Three_count--;
                                result.leftright1--;
                            }
                        }
                    }
                } else if (stone1 == 1 && stone2 == 1) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.leftright2++;
                        if (blank1 == 1) {
                            if (data[chX][chY - (stone1 + blank1)] == turn) {
                                result.Three_count--;
                                result.leftright2--;
                            }
                        } else if (blank2 == 1) {
                            if (data[chX][chY + (stone2 + blank2)] == turn) {
                                result.Three_count--;
                                result.leftright2--;
                            }
                        }
                    }

                } else if (stone2 == 2) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.leftright3++;
                        if (blank1 == 2 && blank2 == 1) {
                            if (data[chX][chY + (stone2 + blank2)] == turn) {
                                result.Three_count--;
                                result.leftright3--;
                            }
                        }
                    }
                }
            }

            stone1 = 0;
            stone2 = 0;
            stonesum = 0;
            blank1 = 0;
            blank2 = 0;

            // ↑ 탐색
            int tempX = chX - 1;
            while (tempX >= 0 && data[tempX][chY] == turn) {
                stone1++;
                tempX--;
            }
            if (tempX >= 0 && data[tempX][chY] == 0) {
                blank1++;
                tempX--;
            }
            while (tempX >= 0 && data[tempX][chY] == turn) {
                stone1++;
                tempX--;
            }
            if (tempX >= 0 && data[tempX][chY] == 0) {
                blank1++;
                tempX--;
            }

            // ↓ 탐색
            tempX = chX + 1;
            while (tempX <= maxIndex && data[tempX][chY] == turn) {
                stone2++;
                tempX++;
            }
            if (tempX <= maxIndex && data[tempX][chY] == 0) {
                blank2++;
                tempX++;
            }
            while (tempX <= maxIndex && data[tempX][chY] == turn) {
                stone2++;
                tempX++;
            }
            if (tempX <= maxIndex && data[tempX][chY] == 0) {
                blank2++;
                tempX++;
            }

            // 열린 3인지 판단
            stonesum = stone1 + stone2;

            if (stonesum == 2) {

                if (stone1 == 2) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.updown1++;
                        if ((blank1 == 1) && (blank2 == 2)) {
                            if (data[chX - (stone1 + blank1)][chY] == turn) {
                                result.Three_count--;
                                result.updown1--;
                            }
                        }
                    }
                } else if (stone1 == 1 && stone2 == 1) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.updown2++;
                        if (blank1 == 1) {
                            if (data[chX - (stone1 + blank1)][chY] == turn) {
                                result.Three_count--;
                                result.updown2--;
                            }
                        } else if (blank2 == 1) {
                            if (data[chX + (stone2 + blank2)][chY] == turn) {
                                result.Three_count--;
                                result.updown2--;
                            }
                        }
                    }
                } else if (stone2 == 2) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.updown3++;
                        if (blank1 == 2 && blank2 == 1) {
                            if (data[chX + (stone2 + blank2)][chY] == turn) {
                                result.Three_count--;
                                result.updown3--;
                            }
                        }
                    }
                }
            }

            stone1 = 0;
            stone2 = 0;
            stonesum = 0;
            blank1 = 0;
            blank2 = 0;

            // ↖ 탐색
            tempX = chX - 1;
            tempY = chY - 1;
            while (tempX >= 0 && tempY >= 0 && data[tempX][tempY] == turn) {
                stone1++;
                tempX--;
                tempY--;
            }
            if (tempX >= 0 && tempY >= 0 && data[tempX][tempY] == 0) {
                blank1++;
                tempX--;
                tempY--;
            }
            while (tempX >= 0 && tempY >= 0 && data[tempX][tempY] == turn) {
                stone1++;
                tempX--;
                tempY--;
            }
            if (tempX >= 0 && tempY >= 0 && data[tempX][tempY] == 0) {
                blank1++;
                tempX--;
                tempY--;
            }

            // ↘ 탐색
            tempX = chX + 1;
            tempY = chY + 1;
            while (tempX <= maxIndex && tempY <= maxIndex && data[tempX][tempY] == turn) {
                stone2++;
                tempX++;
                tempY++;
            }
            if (tempX <= maxIndex && tempY <= maxIndex && data[tempX][tempY] == 0) {
                blank2++;
                tempX++;
                tempY++;
            }
            while (tempX <= maxIndex && tempY <= maxIndex && data[tempX][tempY] == turn) {
                stone2++;
                tempX++;
                tempY++;
            }
            if (tempX <= maxIndex && tempY <= maxIndex && data[tempX][tempY] == 0) {
                blank2++;
                tempX++;
                tempY++;
            }

            // 열린 3인지 판단
            stonesum = stone1 + stone2;

            if (stonesum == 2) {

                if (stone1 == 2) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.leftuprightdown1++;
                        if ((blank1 == 1) && (blank2 == 2)) {
                            if (data[chX - (stone1 + blank1)][chY - (stone1 + blank1)] == turn) {
                                result.Three_count--;
                                result.leftuprightdown1--;
                            }
                        }
                    }
                } else if (stone1 == 1 && stone2 == 1) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.leftuprightdown2++;
                        if (blank1 == 1) {
                            if (data[chX - (stone1 + blank1)][chY - (stone1 + blank1)] == turn) {
                                result.Three_count--;
                                result.leftuprightdown2--;
                            }
                        } else if (blank2 == 1) {
                            if (data[chX + (stone2 + blank2)][chY + (stone2 + blank2)] == turn) {
                                result.Three_count--;
                                result.leftuprightdown2--;
                            }
                        }
                    }
                } else if (stone2 == 2) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.leftuprightdown3++;
                        if (blank1 == 2 && blank2 == 1) {
                            if (data[chX + (stone2 + blank2)][chY + (stone2 + blank2)] == turn) {
                                result.Three_count--;
                                result.leftuprightdown3--;
                            }
                        }
                    }
                }
            }

            stone1 = 0;
            stone2 = 0;
            stonesum = 0;
            blank1 = 0;
            blank2 = 0;

            // ↙ 탐색
            tempX = chX + 1;
            tempY = chY - 1;
            while (tempX <= maxIndex && tempY >= 0 && data[tempX][tempY] == turn) {
                stone1++;
                tempX++;
                tempY--;
            }
            if (tempX <= maxIndex && tempY >= 0 && data[tempX][tempY] == 0) {
                blank1++;
                tempX++;
                tempY--;
            }
            while (tempX <= maxIndex && tempY >= 0 && data[tempX][tempY] == turn) {
                stone1++;
                tempX++;
                tempY--;
            }
            if (tempX <= maxIndex && tempY >= 0 && data[tempX][tempY] == 0) {
                blank1++;
                tempX++;
                tempY--;
            }

            // ↗ 탐색
            tempX = chX - 1;
            tempY = chY + 1;
            while (tempX >= 0 && tempY <= maxIndex && data[tempX][tempY] == turn) {
                stone2++;
                tempX--;
                tempY++;
            }
            if (tempX >= 0 && tempY <= maxIndex && data[tempX][tempY] == 0) {
                blank2++;
                tempX--;
                tempY++;
            }
            while (tempX >= 0 && tempY <= maxIndex && data[tempX][tempY] == turn) {
                stone2++;
                tempX--;
                tempY++;
            }
            if (tempX >= 0 && tempY <= maxIndex && data[tempX][tempY] == 0) {
                blank2++;
                tempX--;
                tempY++;
            }

            // 열린 3인지 판단
            stonesum = stone1 + stone2;

            if (stonesum == 2) {

                if (stone1 == 2) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.leftdownrightup1++;
                        if ((blank1 == 1) && (blank2 == 2)) {
                            if (data[chX + (stone1 + blank1)][chY - (stone1 + blank1)] == turn) {
                                result.Three_count--;
                                result.leftdownrightup1--;
                            }
                        }
                    }
                } else if (stone1 == 1 && stone2 == 1) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.leftdownrightup2++;
                        if (blank1 == 1) {
                            if (data[chX + (stone1 + blank1)][chY - (stone1 + blank1)] == turn) {
                                result.Three_count--;
                                result.leftdownrightup2--;
                            }
                        } else if (blank2 == 1) {
                            if (data[chX - (stone2 + blank2)][chY + (stone2 + blank2)] == turn) {
                                result.Three_count--;
                                result.leftdownrightup2--;
                            }
                        }
                    }
                } else if (stone2 == 2) {
                    if (blank1 + blank2 >= 3) {
                        result.Three_count++;
                        result.leftdownrightup3++;
                        if (blank1 == 2 && blank2 == 1) {
                            if (data[chX - (stone2 + blank2)][chY + (stone2 + blank2)] == turn) {
                                result.Three_count--;
                                result.leftdownrightup3--;
                            }
                        }
                    }
                }
            }

        }
        if (result.Three_count >= 1) {
            if (result.leftright1 + result.leftright2 + result.leftright3 == 1) {
                //System.out.println("좌우3");
            }
            if (result.updown1 + result.updown2 + result.updown3 == 1) {
                //System.out.println("상하3");
            }
            if (result.leftuprightdown1 + result.leftuprightdown2 + result.leftuprightdown3 == 1) {
                //System.out.println("좌상우하3");
            }
            if (result.leftdownrightup1 + result.leftdownrightup2 + result.leftdownrightup3 == 1) {
                //System.out.println("좌하우상3");
            }
        }
        return result;
    }

    public static class ThreeCheckResult {

        public int Three_count;
        int leftright1;
        int leftright2;
        int leftright3;
        int updown1;
        int updown2;
        int updown3;
        int leftuprightdown1;
        int leftuprightdown2;
        int leftuprightdown3;
        int leftdownrightup1;
        int leftdownrightup2;
        int leftdownrightup3;
    }
}