// Copyright Verizon Media. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.vespa.hosted.provision.autoscale;

import com.yahoo.config.provision.NodeResources;

/**
 * A resource target to hit for the allocation optimizer.
 * The target is measured in cpu, memory and disk per node in the allocation given by current.
 */
public class ResourceTarget {

    private final boolean adjustForRedundancy;

    /** The target resources per node, assuming the node assignment in current */
    private final double cpu, memory, disk;

    /** The current allocation leading to this target */
    private final AllocatableClusterResources current;

    private ResourceTarget(double cpu, double memory, double disk,
                           boolean adjustForRedundancy,
                           AllocatableClusterResources current) {
        this.cpu = cpu;
        this.memory = memory;
        this.disk = disk;
        this.adjustForRedundancy = adjustForRedundancy;
        this.current = current;
    }

    /** Are the target resources given by this including redundancy or not */
    public boolean adjustForRedundancy() { return adjustForRedundancy; }
    
    /** Returns the target total cpu to allocate to the entire cluster */
    public double clusterCpu() { return nodeCpu() * current.nodes(); }

    /** Returns the target total memory to allocate to each group */
    public double groupMemory() { return nodeMemory() * current.groupSize(); }

    /** Returns the target total disk to allocate to each group */
    public double groupDisk() { return nodeDisk() * current.groupSize(); }

    /** Returns the target cpu per node, in terms of the current allocation */
    public double nodeCpu() { return cpu; }

    /** Returns the target memory per node, in terms of the current allocation */
    public double nodeMemory() { return memory; }

    /** Returns the target disk per node, in terms of the current allocation */
    public double nodeDisk() { return disk; }

    private static double nodeUsage(Resource resource, double load, AllocatableClusterResources current) {
        return load * resource.valueFrom(current.realResources());
    }

    /** Create a target of achieving ideal load given a current load */
    public static ResourceTarget idealLoad(double currentCpuLoad, double currentMemoryLoad, double currentDiskLoad,
                                           AllocatableClusterResources current) {
        return new ResourceTarget(nodeUsage(Resource.cpu, currentCpuLoad, current) / Resource.cpu.idealAverageLoad(),
                                  nodeUsage(Resource.memory, currentMemoryLoad, current) / Resource.memory.idealAverageLoad(),
                                  nodeUsage(Resource.disk, currentDiskLoad, current) / Resource.disk.idealAverageLoad(),
                                  true,
                                  current);
    }

    /** Crete a target of preserving a current allocation */
    public static ResourceTarget preserve(AllocatableClusterResources current) {
        return new ResourceTarget(current.realResources().vcpu(),
                                  current.realResources().memoryGb(),
                                  current.realResources().diskGb(),
                                  false,
                                  current);
    }

}