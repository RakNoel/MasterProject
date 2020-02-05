//
// Created by RakNoel on 04/02/2020.
//

static int VectorCardinality(std::vector<bool> v) {
    int cnt = 0;
    for (int i = 0; i < v.size(); i++)
        if (v.at(i)) i++;
    return cnt;
}

static int HammingDistance(std::vector<bool> v1, std::vector<bool> v2) {
    //TODO this will be a lot faster with xor
    int diff = 0;
    if (v1.size() != v2.size()) throw std::exception("Vectors not of equal length");
    for (int i = 0; i < v1.size(); i++)
        if (v1.at(i) != v2.at(i)) diff++;
    return diff;
}