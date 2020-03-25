// Copyright Verizon Media. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.vespa.hosted.provision.autoscale;

import com.yahoo.config.provision.ClusterSpec;
import com.yahoo.config.provision.Flavor;
import com.yahoo.config.provision.NodeResources;
import com.yahoo.vespa.hosted.provision.Node;
import com.yahoo.vespa.hosted.provision.provisioning.HostResourcesCalculator;

import java.util.List;

/**
 * @author bratseth
 */
public class AllocatableClusterResources {

    /** The node count in the cluster */
    private final int nodes;

    /** The number of node groups in the cluster */
    private final int groups;

    private final NodeResources realResources;
    private final NodeResources advertisedResources;

    private final ClusterSpec.Type clusterType;

    public AllocatableClusterResources(List<Node> nodes, HostResourcesCalculator calculator) {
        this.advertisedResources = nodes.get(0).flavor().resources();
        this.realResources = calculator.realResourcesOf(nodes.get(0));
        this.nodes = nodes.size();
        this.groups = (int)nodes.stream().map(node -> node.allocation().get().membership().cluster().group()).distinct().count();
        this.clusterType = nodes.get(0).allocation().get().membership().cluster().type();
    }

    public AllocatableClusterResources(ClusterResources realResources,
                                       NodeResources advertisedResources,
                                       ClusterSpec.Type clusterType) {
        this.realResources = realResources.nodeResources();
        this.advertisedResources = advertisedResources;
        this.nodes = realResources.nodes();
        this.groups = realResources.groups();
        this.clusterType = clusterType;
    }

    public AllocatableClusterResources(ClusterResources realResources,
                                       Flavor flavor,
                                       ClusterSpec.Type clusterType,
                                       HostResourcesCalculator calculator) {
        this.realResources = realResources.nodeResources();
        this.advertisedResources = calculator.advertisedResourcesOf(flavor);
        this.nodes = realResources.nodes();
        this.groups = realResources.groups();
        this.clusterType = clusterType;
    }

    /**
     * Returns the resources which will actually be available per node in this cluster with this allocation.
     * These should be used for reasoning about allocation to meet measured demand.
     */
    public NodeResources realResources() { return realResources; }

    /**
     * Returns the resources advertised by the cloud provider, which are the basis for charging
     * and which must be used in resource allocation requests
     */
    public NodeResources advertisedResources() { return advertisedResources; }

    public double cost() { return nodes * Autoscaler.costOf(advertisedResources); }

    public int nodes() { return nodes; }
    public int groups() { return groups; }
    public ClusterSpec.Type clusterType() { return clusterType; }

    @Override
    public String toString() {
        return "$" + cost() + ": " + realResources();
    }

}
