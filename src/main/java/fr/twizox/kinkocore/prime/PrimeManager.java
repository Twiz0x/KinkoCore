package fr.twizox.kinkocore.prime;

import com.j256.ormlite.dao.Dao;
import fr.twizox.kinkocore.AbstractManager;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class PrimeManager extends AbstractManager<Prime> {

    public PrimeManager(Dao<Prime, String> primeDao) {
        super(primeDao);
    }

    public List<Prime> getPrimes() {
        return super.getEntitiesList();
    }

    public List<Prime> getSortedPrimes(Comparator<Prime> comparator) {
        List<Prime> primes = getPrimes();
        primes.sort(comparator);
        return primes;
    }

    public List<Prime> getSortedPrimesByAmount() {
        return getSortedPrimes(Prime::compareTo);
    }

    public boolean hasPrime(UUID uuid) {
        return super.isCached(uuid.toString());
    }

    public Prime getPrime(UUID uuid) {
        return super.getEntity(uuid.toString());
    }

    @Override
    public void cache(Prime prime) {
        super.cache(prime.getTarget().toString(), prime);
    }

    public void uncache(UUID uuid) {
        super.uncache(uuid.toString());
    }

    public void savePrime(Prime prime) {
        super.saveEntity(prime);
    }

    public void cacheAndSave(Prime prime) {
        super.loadAndSave(prime.getTarget().toString(), prime);
    }

    public void deletePrime(Prime prime) {
        super.deleteEntity(prime);
    }

    public void loadPrime(UUID uuid) {
        super.loadEntity(uuid.toString());
    }

}
