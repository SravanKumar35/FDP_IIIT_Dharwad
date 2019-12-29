package org.iiitdharwad.navigation.Objects;

public class FDPValue {
    private String FDPName;

    public String getFDPName() {
        return FDPName;
    }

    public void setFDPName(String FDPName) {
        this.FDPName = FDPName;
    }

    public static class resourcePersonValue {

        private String personName, company, category, heading;
        private int resId;

        public String getCategory() {
            return category;
        }

        public String getCompany() {
            return company;
        }

        public String getPersonName() {
            return personName;
        }

        public int getResId() {
            return resId;
        }

        public String getHeading() {
            return heading;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public void setHeading(String heading) {
            this.heading = heading;
        }
    }
}

